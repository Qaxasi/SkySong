package com.mycompany.SkySong.identity.authentication.adapter.out.session.redis;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.identity.authentication.domain.UserTag;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
public class RedisSessionStore implements SessionStore {
    private static final Logger log = LoggerFactory.getLogger(RedisSessionStore.class);
    private final StringRedisTemplate redis;
    private final DefaultRedisScript<Long> saveScript;
    private final DefaultRedisScript<Long> rotateRefreshTokenScript;
    private final DefaultRedisScript<Long> deleteUserSessionsScript;
    private final RefreshTokenHasher refreshTokenHasher;
    private final SessionJsonSerde serde;
    private final RedisSessionKeyBuilder key;

    public RedisSessionStore(final StringRedisTemplate redis,
                             @Qualifier("saveSessionScript")
                             final DefaultRedisScript<Long> saveScript,
                             @Qualifier("rotateRefreshTokenScript")
                             final DefaultRedisScript<Long> rotateRefreshTokenScript,
                             @Qualifier("deleteUserSessionsScript")
                             final DefaultRedisScript<Long> deleteUserSessionsScript,
                             final RefreshTokenHasher refreshTokenHasher,
                             final SessionJsonSerde serde, RedisSessionKeyBuilder key) {
        this.redis = redis;
        this.saveScript = saveScript;
        this.rotateRefreshTokenScript = rotateRefreshTokenScript;
        this.deleteUserSessionsScript = deleteUserSessionsScript;
        this.refreshTokenHasher = refreshTokenHasher;
        this.serde = serde;
        this.key = key;
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
                                key.refreshTokenSessionKey(userTag, hash),
                                key.refreshTokenHashesKey(userTag)),
                        json,
                        convertToTtlSecondsAsString(ttl),
                        hash);

                if (res == null) {
                    log.error("lua result is null {}", kv("op", "session.save"));
                    return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                }
                if (res == 1L) {
                    return Result.success();
                }

                log.error("unexpected lua result {} {}",
                      kv("op", "session.save"),
                      kv("response", res));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

            } catch (DataAccessException ex) {
                log.error("unexpected redis error {}", kv("op", "session.save"));
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
            final String json = redis.opsForValue().get(key.refreshTokenSessionKey(userTag, hash));
            if (json == null) {
                return Result.failure("Invalid refresh token", ErrorType.REFRESH_TOKEN_NOT_FOUND);
            }

            return serde.deserialize(json);
        } catch (DataAccessException ex) {
            log.error("unexpected redis error {}", kv("op", "session.find_by_refresh_token"), ex);
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
                                        key.refreshTokenSessionKey(userTag, oldTokenHash),
                                        key.refreshTokenSessionKey(userTag, newTokenHash),
                                        key.refreshTokenHashesKey(userTag)),
                                json,
                                convertToTtlSecondsAsString(ttl),
                                oldTokenHash,
                                newTokenHash);

                        if (res == null) {
                            log.error("lua result is null {}", kv("op", "session.rotate_refresh_token"));
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

                        log.error("unexpected lua result {} {}",
                                kv("op", "session.rotate_refresh_token"),
                                kv("response", res));

                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

                    } catch (DataAccessException ex) {
                        log.error("unexpected redis error {}", kv("op", "session.rotate_refresh_token"), ex);
                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                    }
                });
    }

    @Override
    public Result<Void> deleteUserSessions(final UserTag userTag) {
        try {
            final Long deleted = redis.execute(
                    deleteUserSessionsScript,
                    List.of(key.refreshTokenHashesKey(userTag)),
                            key.prefix(userTag));

            if (deleted == null) {
                log.error("lua script returned null {}", kv("op", "session.delete_user_sessions"));
                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
            }

            return Result.success();
        } catch (DataAccessException ex) {
            log.error("unexpected redis error {}", kv("op", "session.delete_user_sessions"), ex);
            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
        }
    }
    private static String convertToTtlSecondsAsString(final Duration ttl) {
        if (ttl.isNegative() || ttl.isZero()) {
            return "1";
        }
        return Long.toString(ttl.toSeconds());
    }
}
