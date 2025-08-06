package com.mycompany.SkySong.spotify.authentication.adapter.out.redis;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.spotify.config.SpotifyRefreshTokenProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class TokenStore {
    private final RedisTemplate<String, String> redisTemplate;
    private final ApplicationLogger logger;
    private final Duration refreshTokenTtl;

    public TokenStore(final RedisTemplate<String, String> redisTemplate,
                      final SpotifyRefreshTokenProperties properties,
                      final ApplicationLogger logger) {
        this.logger = logger;
        this.redisTemplate = redisTemplate;
        this.refreshTokenTtl = properties.ttlAsDuration();
    }

    public Result<Void> saveRefreshToken(final int userId, final String refreshToken) {
        final String redisKey = generateRefreshTokenKey(userId);

        try {
            redisTemplate.opsForValue().set(redisKey, refreshToken, refreshTokenTtl);
            return Result.success();
        } catch (RedisConnectionFailureException ex) {
            logger.error("Redis connection failed while saving token", ex, context("userId", userId));
            return Result.failure("Token storage temporarily unavailable", ErrorType.REDIS_UNAVAILABLE);
        } catch (DataAccessException ex) {
            logger.error("Unexpected Redis error during token persistence", ex, context("userId", userId));
            return Result.failure("Unexpected error occurred while saving token", ErrorType.REDIS_INTERNAL_ERROR);
        }
    }

    public Result<String> getRefreshToken(final int userId) {
        final String redisKey = generateRefreshTokenKey(userId);

        try {
            final String token = redisTemplate.opsForValue().get(redisKey);

            if (token == null || token.isBlank()) {
                logger.info("No refresh token found for user", context("userId", userId));
                return Result.failure("Refresh token not found", ErrorType.TOKEN_NOT_FOUND);
            }
            return Result.success(token);
        } catch (RedisConnectionFailureException ex) {
            logger.error("Redis connection failed while fetching token", ex, context("userId", userId));
            return Result.failure("Token storage temporarily unavailable", ErrorType.REDIS_UNAVAILABLE);
        } catch (DataAccessException ex) {
            logger.error("Unexpected Redis error while fetching token", ex, context("userId", userId));
            return Result.failure("Unexpected error occurred while fetching token", ErrorType.REDIS_INTERNAL_ERROR);
        }
    }
    private String generateRefreshTokenKey(final int userId) {
        return String.format("refresh_token:spotify:user:%d", userId);
    }
}
