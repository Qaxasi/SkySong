package com.mycompany.SkySong.adapter.spotify.authentication.store;

import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(int userId, String refreshToken) {
        String redisKey = generateRedisKey(userId);
        redisTemplate.opsForValue().set(redisKey, refreshToken);
    }

    public Result<String> getRefreshToken(int userId) {
        String redisKey = generateRedisKey(userId);
        String refreshToken = redisTemplate.opsForValue().get(redisKey);

        if (refreshToken == null || refreshToken.isEmpty()) {
            return Result.failure("Refresh token not found for user with ID: " + userId, ErrorType.NOT_FOUND);
        }
        return Result.success(refreshToken);
    }

    private String generateRedisKey(int userId) {
        return "spotify:refresh_token:" + userId;
    }
}
