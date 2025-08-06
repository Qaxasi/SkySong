package com.mycompany.SkySong.spotify.authentication.application.login;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.spotify.authentication.adapter.out.redis.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Service
public class SpotifyAuthorization {
    private final SpotifyTokenClient spotifyClient;
    private final TokenStore tokenStore;
    private final ApplicationLogger logger;

    public SpotifyAuthorization(final SpotifyTokenClient api,
                                final TokenStore tokenStore,
                                final ApplicationLogger logger) {
        this.spotifyClient = api;
        this.tokenStore = tokenStore;
        this.logger = logger;
    }


    public Result<String> authenticateAndReturnToken(final Integer userId, final String authCode) {
        return validateInputs(userId, authCode)
                .flatMap(ignored -> spotifyClient.exchangeAuthorizationCode(authCode)
                .flatMap(response -> saveRefreshToken(userId, response.refreshToken())
                        .map(ignored2 -> response.accessToken())));
    }

    private Result<Void> saveRefreshToken(final int userId, final String refreshToken) {
        final Result<Void> saveResult = tokenStore.saveRefreshToken(userId, refreshToken);
        if (saveResult.isFailure()) {
            logger.warn("Failed to persist Spotify refresh token",
                    context(Map.of("userId", userId, "error", saveResult.errorMessage(), "errorType", saveResult.errorType())));
            return Result.failure("Could not store Spotify refresh token", ErrorType.SPOTIFY_SESSION_PERSISTENCE_FAILED);
        }
        logger.info("New Spotify refresh token successfully stored", context("userId", userId));
        return Result.success();
    }

    private Result<Void> validateInputs(final Integer userId, final String authCode) {
        if (authCode == null || authCode.isBlank()) {
            return Result.failure("Authorization code is missing or blank", ErrorType.VALIDATION_ERROR);
        }
        if (userId == null || userId <= 0) {
            return Result.failure("Missing or invalid user ID", ErrorType.VALIDATION_ERROR);
        }
        return Result.success();
    }
}
