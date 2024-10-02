package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.spotify.exception.RefreshTokenNotFoundException;
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
        String refreshToken = redisTemplate.opsForValue().get(redisKey);

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RefreshTokenNotFoundException("Refresh token not found for user with ID: " + userId);
        }
        return refreshToken;
    }

    private String generateRedisKey(int userId) {
        return "spotify:refresh_token:" + userId;
    }
}
