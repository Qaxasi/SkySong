package com.mycompany.SkySong.adapter.user.token.refresh.redis;

import com.mycompany.SkySong.application.user.token.refresh.dto.SessionUser;
import com.mycompany.SkySong.application.user.token.refresh.ports.SessionUserStore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
class RedisRefreshTokenUserStore implements SessionUserStore {
    private final RedisTemplate<String, SessionUser> redisTemplate;

    RedisRefreshTokenUserStore(RedisTemplate<String, SessionUser> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String refreshToken, SessionUser user) {
        redisTemplate.opsForValue().set(refreshToken, user, Duration.ofDays(1));
    }

    @Override
    public SessionUser getUserByRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }
}
