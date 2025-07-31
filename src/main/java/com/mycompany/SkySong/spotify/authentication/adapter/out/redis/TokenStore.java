package com.mycompany.SkySong.spotify.authentication.adapter.out.redis;

import com.mycompany.SkySong.adapter.exception.external.RedisUnavailableException;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class RedisTokenStore {
    private final RedisTemplate<String, String> redisTemplate;
    private final ApplicationLogger logger;

    public RedisTokenStore(final RedisTemplate<String, String> redisTemplate,
                           final ApplicationLogger logger) {
        this.logger = logger;
        this.redisTemplate = redisTemplate;
    }

    public Result<Void> saveRefreshToken(final int userId, final String refreshToken) {
        final String redisKey = generateRefreshTokenKey(userId);

        try {
            redisTemplate.opsForValue().set(redisKey, refreshToken);
        } catch (DataAccessException ex) {
            if (ex instanceof RedisConnectionFailureException) {
                logger.error("Redis connection failed while saving token", ex, context("userId", userId));
                throw new RedisUnavailableException("Redis is currently unavailable", ErrorType.REDIS_UNAVAILABLE);
            }
            logger.error("Unexpected Redis error while saving token", ex, context("userId", userId));
            throw new RedisUnavailableException("Unexpected Redis error occurred while saving refresh token", ErrorType.REDIS_INTERNAL_ERROR);
        }
    }

    public Result<String> getRefreshToken(final int userId) {
        final String redisKey = generateRefreshTokenKey(userId);

        try {
            final String token = redisTemplate.opsForValue().get(redisKey);

            if (token == null || token.isBlank()) {
                logger.info("No refresh token found in Redis", context("userId", userId));
                return Result.failure("Refresh token not found", ErrorType.TOKEN_NOT_FOUND);
            }
            return Result.success(token);
        } catch (DataAccessException ex) {
            if (ex instanceof RedisConnectionFailureException) {
                logger.error("Redis connection failure while fetching token", ex, context("userId", userId));
                return Result.failure("Redis is currently unavailable", ErrorType.REDIS_UNAVAILABLE);
            }
            logger.error("Unexpected Redis error while fetching token", ex, context("userId", userId));
            return Result.failure("Unexpected Redis error occurred", ErrorType.REDIS_INTERNAL_ERROR);
        }
    }
    private String generateRefreshTokenKey(final int userId) {
        return "music:refresh_token:" + userId;
    }
}
