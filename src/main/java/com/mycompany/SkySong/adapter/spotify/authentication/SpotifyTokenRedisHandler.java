package com.mycompany.SkySong.adapter.spotify.authentication;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpotifyTokenRedisHandler {

    private final RedisTemplate<String, String> redisTemplate;

    public SpotifyTokenRedisHandler(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(int userId, String refreshToken) {
        String redisKey = generateRedisKey(userId);
        redisTemplate.opsForValue().set(redisKey, refreshToken);
    }

    public String getRefreshToken(int userId) {
        String redisKey = generateRedisKey(userId);
        return redisTemplate.opsForValue().get(redisKey);
    }

    private String generateRedisKey(int userId) {
        return "spotify:refresh_token:" + userId;
    }
}
