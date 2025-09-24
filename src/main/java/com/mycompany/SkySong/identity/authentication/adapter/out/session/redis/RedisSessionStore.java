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

import java.util.List;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SessionStoreRedis implements SessionStore {
    private final StringRedisTemplate redis;
    private final DefaultRedisScript<Long> saveScript;
    private final DefaultRedisScript<Long> rotateScript;
    private final DefaultRedisScript<Long> deleteUserSessionsScript;
    private final RefreshTokenHasher hasher;
    private final ApplicationLogger logger;
    private final ObjectMapper objectMapper;

    public SessionStoreRedis(final StringRedisTemplate redis,
                             @Qualifier("saveRefreshTokenScript")
                             final DefaultRedisScript<Long> saveScript,
                             @Qualifier("rotateRefreshTokenScript")
                             final DefaultRedisScript<Long> rotateScript,
                             @Qualifier("deleteUserSessions")
                             final DefaultRedisScript<Long> deleteUserSessionsScript,
                             final RefreshTokenHasher hasher,
                             final ApplicationLogger logger,
                             final ObjectMapper objectMapper) {
        this.redis = redis;
        this.saveScript = saveScript;
        this.rotateScript = rotateScript;
        this.deleteUserSessionsScript = deleteUserSessionsScript;
        this.hasher = hasher;
        this.logger = logger;
        this.objectMapper = objectMapper;
    }

    @Override
    public Result<Void> save(final String userTag, final String refreshToken, final Session session, final long ttlSeconds) {
        final Result<String> hashRes = hasher.hash(refreshToken);
        if (hashRes.isFailure()) {
            return hashRes.propagateFailure();
        }

        final String hash = hashRes.get();

        return serialize(session)
                .flatMap(json -> {
            try {
                final Long res = redis.execute(
                        saveScript,
                        List.of(
                                refreshTokenKeyByHash(userTag, hash),
                                refreshTokenHashesSetKey(userTag)),
                        json,
                        String.valueOf(ttlSeconds), hash);

                if (res == null) {
                    logger.error("session.save - lua result is null");
                    return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                }
                if (res == 1L) {
                    return Result.success();
                }

                logger.error("session.save - unexpected lua result", context("response", res));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

            } catch (DataAccessException ex) {
                logger.error("session.save - unexpected redis error", ex);
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }
        });
    }

    @Override
    public Result<Session> find(final String refreshToken, final String userTag) {
        final Result<String> hashRes = hasher.hash(refreshToken);
        if (hashRes.isFailure()) {
            return hashRes.propagateFailure();
        }

        final String hash = hashRes.get();
        try {
            final String json = redis.opsForValue().get(refreshTokenKeyByHash(userTag, hash));
            if (json == null) {
                return Result.failure("Invalid refresh token", ErrorType.REFRESH_TOKEN_NOT_FOUND);
            }

            return deserialize(json);
        } catch (DataAccessException ex) {
            logger.error("session.find - unexpected redis error", ex);
            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
        }
    }

    @Override
    public Result<Void> rotateRefreshTokenAndUpdateSession(final String userTag,
                                                           final String oldToken,
                                                           final String newToken,
                                                           final Session session,
                                                           final long ttlSeconds) {
        final Result<String> oldTokenHashResult = hasher.hash(oldToken);
        if (oldTokenHashResult.isFailure()) {
            return oldTokenHashResult.propagateFailure();
        }

        final Result<String> newTokenHashResult = hasher.hash(newToken);
        if (newTokenHashResult.isFailure()) {
            return newTokenHashResult.propagateFailure();
        }

        final String oldTokenHash = oldTokenHashResult.get();
        final String newTokenHash = newTokenHashResult.get();

        return serialize(session)
                .flatMap(json -> {
                    try {
                        final Long res = redis.execute(
                                rotateScript,
                                List.of(
                                        refreshTokenKeyByHash(userTag, oldTokenHash),
                                        refreshTokenKeyByHash(userTag, newTokenHash),
                                        refreshTokenHashesSetKey(userTag)),
                                json,
                                String.valueOf(ttlSeconds),
                                oldTokenHash,
                                newTokenHash);

                        if (res == null) {
                            logger.error("session.rotate_refresh_token_and_persist - lua result is null");
                            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                        }
                        if (res == 1L) {
                            return Result.success();
                        }
                        if (res == 0L) {
                            return Result.failure("Invalid refresh token", ErrorType.REFRESH_TOKEN_NOT_FOUND);
                        }
                        if (res == 2L) {
                            return Result.failure("Refresh token rotation conflict", ErrorType.CONFLICT);
                        }

                        logger.error("session.rotate_refresh_token_and_persist - unexpected lua result", context("response", res));
                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

                    } catch (DataAccessException ex) {
                        logger.error("session.rotate_refresh_token_and_persist - unexpected redis error", ex);
                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                    }
                });
    }

    @Override
    public Result<Void> deleteAllForUser(final String userTag) {
        try {
            final Long deleted = redis.execute(
                    deleteUserSessionsScript,
                    List.of(refreshTokenHashesSetKey(userTag)),
                            refreshTokenPrefix(userTag));

            if (deleted == null) {
                logger.error("session.delete_all_for_user - lua script returned null");
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }

            return Result.success();
        } catch (DataAccessException ex) {
            logger.error("session.delete_all_for_user - unexpected redis error", ex);
            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
        }
    }


    private String refreshTokenPrefix(final String userTag) {
        return String.format("auth:rt:{%s}:", userTag);
    }

    private String refreshTokenKeyByHash(final String userTag, final String hash) {
        return refreshTokenPrefix(userTag) + hash;
    }

    private String refreshTokenHashesSetKey(final String userTag) {
        return refreshTokenPrefix(userTag) + "hashes";
    }

    private Result<String> serialize(final Session data) {
        try {
            return Result.success(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException ex) {
            logger.error("session.serialize - json processing error", ex);
            return Result.failure("Internal serialization error", ErrorType.SERIALIZATION_ERROR);
        }
    }

    private Result<Session> deserialize(final String json) {
        try {
            final Session raw = objectMapper.readValue(json, Session.class);

            final Result<Session> rebuilt = Session.create(raw.userId(), raw.username(), raw.roles(), raw.issueAt(), raw.refreshTokenExpiresAt());
            if (rebuilt.isFailure()) {
                logger.error("session.deserialize - invalid payload in store", context("error", rebuilt.errorMessage()));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }

            return rebuilt;
        } catch (JsonProcessingException ex) {
            logger.error("session.deserialize - json processing error", ex);
            return Result.failure("Internal deserialization error", ErrorType.DESERIALIZATION_ERROR);
        }
    }
}
