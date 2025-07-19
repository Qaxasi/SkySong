package com.mycompany.SkySong.spotify.authentication;

import com.mycompany.SkySong.adapter.exception.external.RedisUnavailableException;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(int userId, String refreshToken) {
        String redisKey = generateRefreshTokenKey(userId);

        try {
            redisTemplate.opsForValue().set(redisKey, refreshToken);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed while saving token for user id: {}", userId, ex);
            throw new RedisUnavailableException(
                    "Redis is currently unavailable",
                    ErrorType.REDIS_UNAVAILABLE);
        } catch (DataAccessException ex) {
            log.error("Unexpected Redis error while saving token for user id: {}", userId, ex);
            throw new RedisUnavailableException(
                    "Unexpected Redis error occurred while saving refresh token",
                    ErrorType.REDIS_INTERNAL_ERROR);
        }
    }

    public Result<String> getRefreshToken(int userId) {
        String redisKey = generateRefreshTokenKey(userId);

        try {
            String token = redisTemplate.opsForValue().get(redisKey);

            if (token == null || token.isBlank()) {
                log.info("No refresh token found in Redis for user id: {}", userId);
                return Result.failure(
                        "Refresh token not found. Spotify reauthorization required",
                        ErrorType.TOKEN_NOT_FOUND);
            }
            return Result.success(token);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis is unavailable", ex);
            return Result.failure(
                    "Redis is currently unavailable",
                    ErrorType.REDIS_UNAVAILABLE);
        } catch (DataAccessException ex) {
            log.error("Unexpected Redis error", ex);
            return Result.failure(
                    "Unexpected Redis error occurred",
                    ErrorType.REDIS_INTERNAL_ERROR);
        }
    }

    private String generateRefreshTokenKey(int userId) {
        return "music:refresh_token:" + userId;
    }
}
