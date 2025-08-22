package com.mycompany.SkySong.spotify.authentication.adapter.out.redis;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.infrastructure.spotify.config.SpotifyRefreshTokenProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SpotifyTokenStoreRedis {

    private final StringRedisTemplate redisTemplate;
    private final ApplicationLogger logger;
    private final Duration refreshTokenTtl;

    public SpotifyTokenStoreRedis(final StringRedisTemplate redisTemplate,
                           final SpotifyRefreshTokenProperties properties,
                           final ApplicationLogger logger) {
        this.redisTemplate = redisTemplate;
        this.logger = logger;
        this.refreshTokenTtl = properties.ttlAsDuration();
    }

    public Result<Void> saveRefreshToken(final int userId, final String refreshToken) {
        if (userId <= 0) {
            return Result.failure("Invalid user id", ErrorType.VALIDATION_ERROR)
        }
        final String redisKey = generateRefreshTokenKey(userId);

        try {
            redisTemplate.opsForValue().set(redisKey, refreshToken, refreshTokenTtl);
            return Result.success();
        } catch (RedisConnectionFailureException ex) {
            logger.error("Failed to save refresh token - Redis connection failed", context("userId", userId), ex);
            return Result.failure("Token storage temporarily unavailable", ErrorType.REDIS_UNAVAILABLE);
        } catch (DataAccessException ex) {
            logger.error("Failed to save refresh token - unexpected Redis error", context("userId", userId), ex);
            return Result.failure("Unexpected error occurred while saving token", ErrorType.REDIS_INTERNAL_ERROR);
        }
    }

    public Result<String> getRefreshToken(final int userId) {
        final String redisKey = generateRefreshTokenKey(userId);

        try {
            final String token = redisTemplate.opsForValue().get(redisKey);
            return Result.success(token);
        } catch (RedisConnectionFailureException ex) {
            logger.error("Failed to fetch refresh token - Redis connection failed", context("userId", userId), ex);
            return Result.failure("Token storage temporarily unavailable", ErrorType.REDIS_UNAVAILABLE);
        } catch (DataAccessException ex) {
            logger.error("Failed to fetch refresh token - unexpected Redis error", context("userId", userId), ex);
            return Result.failure("Unexpected error occurred while fetching token", ErrorType.REDIS_INTERNAL_ERROR);
        }
    }

    public Result<Void> deleteForUser(final int userId) {
        if (userId <= 0) {
            return Result.failure("x", ErrorType.VALIDATION_ERROR); // check
        }

        try {
            redisTemplate.delete()
        }
    }
    private String generateRefreshTokenKey(final int userId) {
        return String.format("refresh_token:spotify:user:%d", userId);
    }
}
