package com.mycompany.SkySong.spotify.authentication.application.refresh;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.spotify.authentication.adapter.out.redis.TokenStore;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Service
@Slf4j
public class SpotifyAccessTokenRefresher {
    private final SpotifyTokenClient spotifyClient;
    private final TokenStore tokenStore;
    private final ApplicationLogger logger;

    public SpotifyAccessTokenRefresher(final SpotifyTokenClient api,
                                       final TokenStore tokenStore,
                                       final ApplicationLogger logger) {
        this.spotifyClient = api;
        this.tokenStore = tokenStore;
        this.logger = logger;
    }

    public Result<String> refreshAccessToken(final Integer userId) {
        if (userId == null || userId <= 0) {
            return Result.failure("Missing or invalid user ID", ErrorType.VALIDATION_ERROR);
        }

        return tokenStore.getRefreshToken(userId)
                .flatMap(token -> {
                        if (token == null || token.isBlank()) {
                            logger.warn("No spotify refresh token found for user", context("userId", userId));
                            return Result.failure("Spotify session not found", ErrorType.SPOTIFY_SESSION_NOT_FOUND);
                        }

                        return spotifyClient.exchangeRefreshToken(token)
                                .flatMap(response -> {
                                    saveRefreshTokenIfPresent(response, userId);
                                    return Result.success(response.accessToken());
                                });
                });
    }
    private void saveRefreshTokenIfPresent(final SpotifyTokenResponse response, final int userId) {
        if (response.refreshToken() != null && !response.refreshToken().isBlank()) {
            final Result<Void> saveResult = tokenStore.saveRefreshToken(userId, response.refreshToken());
            if (saveResult.isFailure()) {
                logger.warn("Failed to save Spotify refresh token",
                        context(Map.of(
                                "userId", userId,
                                "error", saveResult.errorMessage(),
                                "errorType", saveResult.errorType())));
            } else {
                logger.info("New Spotify refresh token stored for user", context("userId", userId));
            }
        }
    }
}
