package com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
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

    public Result<String> getRefreshToken(int userId) {
        String redisKey = generateRefreshTokenKey(userId);
        String token =  redisTemplate.opsForValue().get(redisKey);

        if (token == null || token.isBlank()) {
            return Result.failure("Refresh token not found", ErrorType.NOT_FOUND);
        }

        return Result.success(token);
    }

    private String generateRefreshTokenKey(int userId) {
        return "music:refresh_token:" + userId;
    }
}
