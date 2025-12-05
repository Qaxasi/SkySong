package com.mycompany.skysong.identity.adapter.out.redis;

import com.mycompany.skysong.identity.application.port.SessionStore;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.Session;
import com.mycompany.skysong.identity.domain.UserTag;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
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
    private final DefaultRedisScript<Long> saveSessionScript;
    private final DefaultRedisScript<Long> rotateSessionScript;
    private final RefreshTokenHasher refreshTokenHasher;
    private final SessionJsonSerde serde;
    private final RedisSessionKeyBuilder key;

    public RedisSessionStore(final StringRedisTemplate redis,
                             @Qualifier("saveSession")
                             final DefaultRedisScript<Long> saveSessionScript,
                             @Qualifier("rotateSession")
                             final DefaultRedisScript<Long> rotateSessionScript,
                             final RefreshTokenHasher refreshTokenHasher,
                             final SessionJsonSerde serde,
                             final RedisSessionKeyBuilder key) {
        this.redis = redis;
        this.saveSessionScript = saveSessionScript;
        this.rotateSessionScript = rotateSessionScript;
        this.refreshTokenHasher = refreshTokenHasher;
        this.serde = serde;
        this.key = key;
    }

    @Override
    public Result<Unit> saveSession(final UserTag userTag, final RefreshToken token,
                                    final Session session, final Duration ttl) {
        return refreshTokenHasher.hash(token)
                .flatMap(hash ->
                        serde.serialize(session)
                                .flatMap(json -> {
                                    try {
                                        final Long res = redis.execute(
                                                saveSessionScript,
                                                List.of(key.sessionKey(userTag, hash)),
                                                json,
                                                convertToTtlSecondsAsString(ttl));

                                        if (res == null) {
                                            log.error("lua result is null {}",
                                                    kv("op", "session.save"));
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
                                        log.error("unexpected redis error {}",
                                                kv("op", "session.save"),
                                                ex);
                                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                                    }
                                })
                );
    }

    @Override
    public Result<Session> findBy(final UserTag userTag, final RefreshToken refreshToken) {
        return refreshTokenHasher.hash(refreshToken)
                .flatMap(hash ->  {
                    try {
                        final String json = redis.opsForValue().get(key.sessionKey(userTag, hash));
                        if (json == null) {
                            return Result.failure("Session not found", ErrorType.SESSION_NOT_FOUND);
                        }

                        return serde.deserialize(json);
                    } catch (DataAccessException ex) {
                        log.error("unexpected redis error {}",
                                kv("op", "session.find"),
                                ex);
                        return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                    }
                });
    }

    @Override
    public Result<Unit> rotateSession(final UserTag userTag,
                                      final RefreshToken oldToken,
                                      final RefreshToken newToken,
                                      final Session session,
                                      final Duration ttl) {
        return Result
                .combineM(
                        refreshTokenHasher.hash(oldToken),
                        refreshTokenHasher.hash(newToken),
                        (oldHash, newHash) ->
                                serde.serialize(session)
                                    .flatMap(json -> {
                                        try {
                                            final Long res = redis.execute(
                                                    rotateSessionScript,
                                                    List.of(
                                                            key.sessionKey(userTag, oldHash),
                                                            key.sessionKey(userTag, newHash)),
                                                    json,
                                                    convertToTtlSecondsAsString(ttl));

                                            if (res == null) {
                                                log.error("lua result is null {}",
                                                        kv("op", "session.rotate"));
                                                return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                                            }
                                            if (res == 1L) {
                                                return Result.success();
                                            }
                                            if (res == 0L) {
                                                return Result.failure("Session not found", ErrorType.SESSION_NOT_FOUND);
                                            }
                                            if (res == 2L) {
                                                return Result.failure("Rotate session conflict", ErrorType.SESSION_ROTATION_CONFLICT);
                                            }

                                            log.error("unexpected lua result {} {}",
                                                    kv("op", "session.rotate"),
                                                    kv("response", res));

                                            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);

                                        } catch (DataAccessException ex) {
                                            log.error("unexpected redis error {}",
                                                    kv("op", "session.rotate"),
                                                    ex);
                                            return Result.failure("Internal storage error", ErrorType.PERSISTENCE_ERROR);
                                        }
                                    })
                );
    }

    private static String convertToTtlSecondsAsString(final Duration ttl) {
        return Long.toString(ttl.toSeconds());
    }
}
