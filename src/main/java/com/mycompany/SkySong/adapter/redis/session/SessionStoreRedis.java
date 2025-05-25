package com.mycompany.SkySong.adapter.redis.session;

import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;
import com.mycompany.SkySong.application.user.authentication.dto.SessionData;
import com.mycompany.SkySong.application.user.authentication.ports.SessionStore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class SessionStoreRedis implements SessionStore {
    private final RedisTemplate<String, SessionData> redisTemplate;

    public SessionStoreRedis(final RedisTemplate<String, SessionData> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(RefreshToken token, SessionData sessionData) {
        final Instant now = Instant.now();
        final Duration ttl = Duration.between(now, sessionData.expiresAt());
        redisTemplate.opsForValue().set(token.value(), sessionData, ttl);
    }

    @Override
    public Optional<SessionData> findByToken(RefreshToken token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(token.value()));
    }
}
