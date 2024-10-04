package com.mycompany.SkySong.adapter.spotify.authentication.repository;

import com.mycompany.SkySong.adapter.spotify.authentication.exception.RefreshTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(int userId, String refreshToken) {
    if (refreshToken == null || refreshToken.isEmpty()) {
        log.warn("Attempted to save null or empty refresh token for userId: " + userId);
        return;
    }
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
