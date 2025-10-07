package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.identity.shared.domain.UserTag;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class RedisSessionStore implements SessionStore {
    private static final int KEY_PREFIX_VERSION = 1;

    private final StringRedisTemplate redis;
    private final DefaultRedisScript<Long> saveScript;
    private final DefaultRedisScript<Long> rotateRefreshTokenScript;
    private final DefaultRedisScript<Long> deleteUserSessionsScript;
    private final RefreshTokenHasher refreshTokenHasher;
    private final ApplicationLogger logger;
    private final SessionJsonSerde serde;

    public RedisSessionStore(final StringRedisTemplate redis,
                             @Qualifier("saveScript")
                             final DefaultRedisScript<Long> saveScript,
                             @Qualifier("rotateRefreshTokenScript")
                             final DefaultRedisScript<Long> rotateRefreshTokenScript,
                             @Qualifier("deleteUserSessionsScript")
                             final DefaultRedisScript<Long> deleteUserSessionsScript,
                             final RefreshTokenHasher refreshTokenHasher,
                             final ApplicationLogger logger,
                             final SessionJsonSerde serde) {
        this.redis = redis;
        this.saveScript = saveScript;
        this.rotateRefreshTokenScript = rotateRefreshTokenScript;
        this.deleteUserSessionsScript = deleteUserSessionsScript;
        this.refreshTokenHasher = refreshTokenHasher;
        this.logger = logger;
        this.serde = serde;
    }

    @Override
    public Result<Void> save(final UserTag userTag, final RefreshToken token, final Session session, final Duration ttl) {
        final Result<String> hashRes = refreshTokenHasher.hash(token);
        if (hashRes.isFailure()) {
            return hashRes.propagateFailure();
        }

        final String hash = hashRes.get();

        return serde.serialize(session)
                .flatMap(json -> {
            try {
                final Long res = redis.execute(
                        saveScript,
                        List.of(
                                sessionKeyByHash(userTag, hash),
                                sessionHashIndexKey(userTag)),
                        json,
                        convertToTtlSecondsAsString(ttl),
                        hash);

                if (res == null) {
                    logger.error("lua result is null", context("op", "session.save"));
                    return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                }
                if (res == 1L) {
                    return Result.success();
                }

                logger.error("unexpected lua result", context(
                        Map.of("op", "session.save",
                                "response", res)));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

            } catch (DataAccessException ex) {
                logger.error("unexpected redis error", context("op", "session.save"), ex);
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }
        });
    }

    @Override
    public Result<Session> findByRefreshToken(final UserTag userTag, final RefreshToken refreshToken) {
        final Result<String> hashRes = refreshTokenHasher.hash(refreshToken);
        if (hashRes.isFailure()) {
            return hashRes.propagateFailure();
        }

        final String hash = hashRes.get();
        try {
            final String json = redis.opsForValue().get(sessionKeyByHash(userTag, hash));
            if (json == null) {
                return Result.failure("Invalid refresh token", ErrorType.REFRESH_TOKEN_NOT_FOUND);
            }

            return serde.deserialize(json);
        } catch (DataAccessException ex) {
            logger.error("unexpected redis error", context("op", "session.find_by_refresh_token"), ex);
            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
        }
    }

    @Override
    public Result<Void> rotateRefreshToken(final UserTag userTag,
                                           final RefreshToken oldToken,
                                           final RefreshToken newToken,
                                           final Session session,
                                           final Duration ttl) {
        final Result<String> oldTokenHashResult = refreshTokenHasher.hash(oldToken);
        if (oldTokenHashResult.isFailure()) {
            return oldTokenHashResult.propagateFailure();
        }

        final Result<String> newTokenHashResult = refreshTokenHasher.hash(newToken);
        if (newTokenHashResult.isFailure()) {
            return newTokenHashResult.propagateFailure();
        }

        final String oldTokenHash = oldTokenHashResult.get();
        final String newTokenHash = newTokenHashResult.get();

        return serde.serialize(session)
                .flatMap(json -> {
                    try {
                        final Long res = redis.execute(
                                rotateRefreshTokenScript,
                                List.of(
                                        sessionKeyByHash(userTag, oldTokenHash),
                                        sessionKeyByHash(userTag, newTokenHash),
                                        sessionHashIndexKey(userTag)),
                                json,
                                convertToTtlSecondsAsString(ttl),
                                oldTokenHash,
                                newTokenHash);

                        if (res == null) {
                            logger.error("lua result is null", context("op", "session.rotate_refresh_token"));
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

                        logger.error("unexpected lua result",
                                context(Map.of("op", "session.rotate_refresh_token",
                                        "response", res)));

                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

                    } catch (DataAccessException ex) {
                        logger.error("unexpected redis error", context("op", "session.rotate_refresh_token"), ex);
                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                    }
                });
    }

    @Override
    public Result<Void> deleteUserSessions(final UserTag userTag) {
        try {
            final Long deleted = redis.execute(
                    deleteUserSessionsScript,
                    List.of(sessionHashIndexKey(userTag)),
                            sessionKeyPrefix(userTag));

            if (deleted == null) {
                logger.error("lua script returned null", context("op", "session.delete_user_sessions"));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }

            return Result.success();
        } catch (DataAccessException ex) {
            logger.error("unexpected redis error", context("op", "session.delete_user_sessions"), ex);
            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
        }
    }
    private String sessionKeyPrefix(final UserTag userTag) {
        return String.format("auth:rt:v%d:{%s}:", KEY_PREFIX_VERSION, userTag.asBase64Url());
    }

    private String sessionKeyByHash(final UserTag userTag, final String hash) {
        return sessionKeyPrefix(userTag) + hash;
    }

    private String sessionHashIndexKey(final UserTag userTag) {
        return sessionKeyPrefix(userTag) + "hashes";
    }

    private static String convertToTtlSecondsAsString(final Duration ttl) {
        if (ttl.isNegative() || ttl.isZero()) {
            return "1";
        }
        return Long.toString(ttl.toSeconds());
    }
}
