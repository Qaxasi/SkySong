package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SessionStoreRedis implements SessionStore {
    private final StringRedisTemplate redis;
    private final DefaultRedisScript<Long> saveScript;
    private final DefaultRedisScript<Long> rotateScript;
    private final ApplicationLogger logger;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public SessionStoreRedis(final StringRedisTemplate redis,
                             @Qualifier("saveRefreshTokenScript")
                             final DefaultRedisScript<Long> saveScript,
                             @Qualifier("rotateRefreshTokenScript")
                             final DefaultRedisScript<Long> rotateScript,
                             final ApplicationLogger logger,
                             final ObjectMapper objectMapper,
                             final Clock clock) {
        this.redis = redis;
        this.saveScript = saveScript;
        this.rotateScript = rotateScript;
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Override
    public Result<Void> save(final String token, final Session session) {
        final int userId = session.userId();
        final long ttl = Math.max(session.remainingTtlSeconds(clock), 1L);

        return serialize(session).flatMap(json -> {
            try {
                final Long res = redis.execute(
                        saveScript,
                        List.of(mainKey(userId, token), tokenSetKey(userId)),
                        json, String.valueOf(ttl), token);

                if (res == null || res != 1L) {
                    logger.error("Save script returned unexpected result", context("userId", userId));
                    return Result.failure("Unexpected error occurred while saving session", ErrorType.INTERNAL_SERVER_ERROR);
                }

                redis.opsForValue().set(tk2uidKey(token), String.valueOf(userId), Duration.ofSeconds(ttl));
                return Result.success();
            } catch (DataAccessException ex) {
                logger.error("Failed to save session due to database error", ex);
                return Result.failure("Unexpected error occurred while saving session", ErrorType.PERSISTENCE_ERROR);
            }
        });
    }

    @Override
    public Result<Session> findByToken(final String token) {
        try {
            final Optional<Integer> userId = readUserIdForToken(token);
            if (userId.isEmpty()) {
                return Result.failure("Session not found", ErrorType.SESSION_NOT_FOUND);
            }

            final String json = redis.opsForValue().get(mainKey(userId.get(), token));
            if (json == null) {
                cleanupAfterMissingSessionKey(userId.get(), token);
                return Result.failure("Session not found", ErrorType.SESSION_NOT_FOUND);
            }

            return deserialize(json);
        } catch (DataAccessException ex) {
            logger.error("Failed to fetch session due to database error", ex);
            return Result.failure("Unexpected error occurred while fetching session", ErrorType.PERSISTENCE_ERROR);
        }
    }

    @Override
    public Result<Void> rotate(final String oldToken,
                               final String newToken,
                               final Session session) {
        final int userId = session.userId();
        final String oldMainKey = mainKey(userId, oldToken);
        final String newMainKey= mainKey(userId, newToken);
        final String setKey = tokenSetKey(userId);

        final long ttl = Math.max(session.remainingTtlSeconds(clock), 1L);

        return serialize(session).flatMap(json -> {
                    try {
                        final Long res = redis.execute(
                                rotateScript,
                                List.of(oldMainKey, newMainKey, setKey),
                                json, String.valueOf(ttl),
                                oldToken, newToken);

                        if (res == null) {
                            logger.error("Rotate script returned unexpected error", context("userId", userId));
                            return Result.failure("Unexpected error occurred while updating session", ErrorType.INTERNAL_SERVER_ERROR);
                        } else if (res == 0L) {
                            logger.warn("Rotation failed - old refresh token not found", context("userId", userId));
                            return Result.failure("Session not found", ErrorType.SESSION_NOT_FOUND);
                        }

                        redis.opsForValue().set(tk2uidKey(newToken), String.valueOf(userId), Duration.ofSeconds(ttl));
                        redis.delete(tk2uidKey(oldToken));

                        return Result.success();
                    } catch (DataAccessException ex) {
                        logger.error("Failed to update session due to database error", ex);
                        return Result.failure("Unexpected error occurred while updating session", ErrorType.PERSISTENCE_ERROR);
                    }
                });
    }

    @Override
    public Result<Void> deleteAllForUser(final int userId) {
        try {
            final String setKey = tokenSetKey(userId);
            final Set<String> tokens = redis.opsForSet().members(setKey);
            if (tokens == null || tokens.isEmpty()) {
                redis.delete(setKey);
                return Result.success();
            }

            final List<String> mainKeys = tokens.stream().map(t -> mainKey(userId, t)).toList();

            final List<String> mapKeys = tokens.stream().map(this::tk2uidKey).toList();

            if (!mainKeys.isEmpty()) {
                redis.delete(mainKeys);
            }
            if (!mapKeys.isEmpty()) {
                redis.delete(mapKeys);
            }
            redis.delete(setKey);

            return Result.success();

        } catch (DataAccessException ex) {
            logger.error("Failed to delete all sessions for user", context("userId", userId), ex);
            return Result.failure("Unexpected database error during deleting sessions", ErrorType.PERSISTENCE_ERROR);
        }
    }


    private String mainKey(final int userId, final String token) {
        return String.format("auth:refresh:{%d}:tk:%s", userId, token);
    }

    private String tokenSetKey(final int userId) {
        return String.format("auth:refresh:{%d}:tokens", userId);
    }

    private String tk2uidKey(final String key) {
        return String.format("tk2uid:%s", key);
    }

    private void cleanupAfterMissingSessionKey(final int userId, final String token) {
        try {
            redis.delete(tk2uidKey(token));
            redis.opsForSet().remove(tokenSetKey(userId), token);
        } catch (DataAccessException ex) {
            logger.warn("Unable to remove outdated references to the token (no session key)", context("userId", userId));
        }
    }

    private Optional<Integer> readUserIdForToken(final String token) {
        final String key = tk2uidKey(token);
        final String uidString = redis.opsForValue().get(key);
        if (uidString == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(uidString));
        } catch (NumberFormatException ex) {
            logger.warn("Error in token-userId mapping");
            return Optional.empty();
        }
    }

    private Result<String> serialize(final Session data) {
        try {
            return Result.success(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ex) {
            logger.error("Session serialization failed", ex);
            return Result.failure("Failed to serialize session data", ErrorType.SERIALIZATION_ERROR);
        }
    }

    private Result<Session> deserialize(final String json) {
        try {
            final Session raw = objectMapper.readValue(json, Session.class);
            return Session.create(raw.userId(), raw.username(), raw.roles(), raw.issueAt(), raw.refreshTokenExpiresAt());
        } catch (JsonProcessingException ex) {
            logger.error("Session deserialization failed", ex);
            return Result.failure("Failed to deserialize session data", ErrorType.SERIALIZATION_ERROR);
        }
    }
}
