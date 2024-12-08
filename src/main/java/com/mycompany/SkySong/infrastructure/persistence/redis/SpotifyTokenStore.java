package com.mycompany.SkySong.infrastructure.persistence.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpotifyTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public SpotifyTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(int userId, String refreshToken) {
        String redisKey = generateRefreshTokenKey(userId);
        redisTemplate.opsForValue().set(redisKey, refreshToken);
    }

    public String getRefreshToken(int userId) {
        String redisKey = generateRefreshTokenKey(userId);
        return redisTemplate.opsForValue().get(redisKey);
    }

    private String generateRefreshTokenKey(int userId) {
        return "spotify:refresh_token:" + userId;
    }
}
