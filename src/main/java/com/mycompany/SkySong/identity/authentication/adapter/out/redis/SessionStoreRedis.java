package com.mycompany.SkySong.identity.adapter.out.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.identity.adapter.out.redis.exception.SessionStoreException;
import com.mycompany.SkySong.identity.application.authentication.refresh.ports.RefreshTokenRotator;
import com.mycompany.SkySong.identity.application.authentication.shared.dto.SessionData;
import com.mycompany.SkySong.identity.application.authentication.shared.ports.SessionStore;
import com.mycompany.SkySong.identity.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class SessionStoreRedis implements SessionStore, RefreshTokenRotator {
    private final RedisTemplate<String, SessionData> redisTemplate;
    private final String rotateScript;

    public SessionStoreRedis(@Value("classpath:redis-scripts/rotate_refresh_token.lua") final String rotateScript,
                              final RedisTemplate<String, SessionData> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.rotateScript = rotateScript;
    }

    @Override
    public void save(final RefreshToken token, final SessionData sessionData) {
        final Instant now = Instant.now();
        final Duration ttl = Duration.between(now, sessionData.expiresAt());
        try {
            redisTemplate.opsForValue().set(token.value(), sessionData, ttl);
        } catch (RuntimeException e) {
            if (e instanceof NullPointerException
                    || e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new SessionStoreException("Failed to save session", e);
        }
    }

    @Override
    public Optional<SessionData> findByToken(final RefreshToken token) {
        try {
            return Optional.ofNullable(redisTemplate.opsForValue().get(token.value()));
        }  catch (RuntimeException e) {
            if (e instanceof NullPointerException
                    || e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new SessionStoreException("Failed to retrieve session", e);
        }
    }

    @Override
    public void delete(final RefreshToken token) {
        try {
            redisTemplate.delete(token.value());
        } catch (RuntimeException e) {
            if (e instanceof NullPointerException
                    || e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new SessionStoreException("Failed to delete session", e);
        }
    }

    @Override
    public void rotate(final RefreshToken oldToken,
                       final RefreshToken newToken,
                       final SessionData sessionData) {
        final long ttlSeconds = Duration.between(Instant.now(), sessionData.expiresAt()).getSeconds();
        try {
            redisTemplate.execute(
                    new DefaultRedisScript<>(rotateScript, Integer.class),
                    List.of(oldToken.value(), newToken.value()),
                    serialize(sessionData), String.valueOf(ttlSeconds));
        } catch (RuntimeException e) {
            if (e instanceof NullPointerException || e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new SessionStoreException("Failed to atomically rotate refresh token", e);
        }
    }

    private String serialize(SessionData sessionData) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(sessionData);
        } catch (JsonProcessingException e) {
            throw new SessionStoreException("Serialization error", e);
        }
    }
}
