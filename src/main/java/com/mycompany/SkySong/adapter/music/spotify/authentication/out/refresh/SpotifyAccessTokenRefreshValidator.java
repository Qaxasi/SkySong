package com.mycompany.SkySong.adapter.music.spotify.authentication.out.refresh;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyAccessTokenRefreshRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpotifyAccessTokenRefreshValidator {
    public Result<Void> validateRequest(SpotifyAccessTokenRefreshRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            log.warn("Request validation failed: grant type is null or empty");
            return Result.failure(
                    "Grant type cannot be null or empty",
                    ErrorType.USER_REQUEST_INVALID);
        }
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            log.warn("Request validation failed: refresh token is null or empty");
            return Result.failure(
                    "Refresh token cannot be null or empty",
                    ErrorType.USER_REQUEST_INVALID);
        }
        return Result.success(null);
    }

    public Result<Void> validateResponse(SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            log.error("Response validation failed: access token is null or empty");
            return Result.failure(
                    "Access token cannot be null or empty",
                    ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
        return Result.success(null);
    }
}
