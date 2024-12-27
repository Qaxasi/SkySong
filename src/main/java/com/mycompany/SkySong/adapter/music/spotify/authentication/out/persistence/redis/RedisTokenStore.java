package com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenStore(RedisTemplate<String, String> redisTemplate) {
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
        return "music:refresh_token:" + userId;
    }
}
