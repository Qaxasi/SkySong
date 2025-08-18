package com.mycompany.SkySong.identity.authentication.adapter.out.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.identity.authentication.application.refresh.port.RefreshTokenRotator;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

@Component
public class SessionStoreRedis implements SessionStore, RefreshTokenRotator {
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Integer> rotateScript;
    private final ApplicationLogger logger;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public SessionStoreRedis(final StringRedisTemplate redisTemplate,
                             final DefaultRedisScript<Integer> rotateRefreshTokenScript,
                             final ApplicationLogger logger,
                             final ObjectMapper objectMapper,
                             final Clock clock) {
        this.redisTemplate = redisTemplate;
        this.rotateScript = rotateRefreshTokenScript;
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Override
    public Result<Void> save(final String token,
                             final Session session) {
        if (token == null || token.isBlank()) {
            return Result.failure("Token must not be null or blank", ErrorType.INVARIANT_VIOLATION);
        }
        if (session == null) {
            return Result.failure("Missing session data", ErrorType.INVARIANT_VIOLATION);
        }

        final Duration ttl = session.remainingTtl(clock);
        return serialize(session).flatMap(json -> {
            try {
                redisTemplate.opsForValue().set(token, json, ttl);
                return Result.success();
            } catch (DataAccessException ex) {
                logger.error("Failed to save session due to database error", ex);
                return Result.failure("Unexpected error occurred while saving session", ErrorType.PERSISTENCE_ERROR);
            }
        });
    }

    @Override
    public Result<Session> findByToken(final String token) {
        if (token == null || token.isBlank()) {
            return Result.failure("Token must not be null or blank", ErrorType.INVARIANT_VIOLATION);
        }

        try {
            final String json = redisTemplate.opsForValue().get(token);
            if (json == null) {
                return Result.failure("Refresh token not found", ErrorType.REFRESH_TOKEN_NOT_FOUND);
            }
            return deserialize(json);
        } catch (DataAccessException ex) {
            logger.error("Failed to fetch session due to database error", ex);
            return Result.failure("Unexpected error occurred while fetching session", ErrorType.PERSISTENCE_ERROR);
        }
    }

    @Override
    public Result<Void> delete(final String token) {
        if (token == null || token.isBlank()) {
            return Result.failure("Token must not be null or blank", ErrorType.INVARIANT_VIOLATION);
        }

        try {
            redisTemplate.delete(token);
            return Result.success();
        } catch (DataAccessException ex) {
            logger.error("Failed to delete session due to database error", ex);
            return Result.failure("Unexpected error occurred while deleting session", ErrorType.PERSISTENCE_ERROR);
        }
    }

    @Override
    public Result<Void> rotate(final String oldToken,
                               final String newToken,
                               final Session session) {

        if (oldToken == null || oldToken.isBlank()
                || newToken == null || newToken.isBlank()) {
            return Result.failure("Old and new refresh token must not be null or blank", ErrorType.INVARIANT_VIOLATION);
        }
        if (session == null) {
            return Result.failure("Missing session data", ErrorType.INVARIANT_VIOLATION);
        }

        final long ttl = session.remainingTtlSeconds(clock);
        return serialize(session)
                .flatMap(json -> {
                    try {
                        Integer res = redisTemplate.execute(
                                rotateScript,
                                List.of(oldToken, newToken),
                                json, String.valueOf(ttl));

                        if (res == null || res != 1) {
                            return Result.failure("Refresh token not found", ErrorType.REFRESH_TOKEN_NOT_FOUND);
                        }
                        return Result.success();
                    } catch (DataAccessException ex) {
                        logger.error("Failed to rotate refresh token due to database error", ex);
                        return Result.failure("Unexpected error occurred while rotating refresh token", ErrorType.PERSISTENCE_ERROR);
                    }
                });
    }

    private Result<String> serialize(final Session sessionData) {
        try {
            return Result.success(objectMapper.writeValueAsString(sessionData));
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
