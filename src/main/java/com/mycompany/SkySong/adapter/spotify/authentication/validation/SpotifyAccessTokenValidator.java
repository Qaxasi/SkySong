package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.Result;

public class SpotifyAccessTokenValidator {
    public Result<Void> validateRequest(SpotifyAccessTokenRequest request) {
        if (request.grantType() == null || request.grantType().isEmpty()) {
            Result.failure("Grant type cannot be null or empty");
        }
        if (request.authCode() == null || request.authCode().isEmpty()) {
            Result.failure("Authorization code cannot be null or empty");
        }
        if (request.redirectUri() == null || request.redirectUri().isEmpty()) {
            Result.failure("Redirect uri cannot be null or empty");
        }
        return Result.success(null);
    }

    public Result<Void> validateResponse(SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isEmpty()) {
            return Result.failure("Access token cannot be null or empty");
        }
        if (response.refreshToken() == null || response.refreshToken().isEmpty()) {
            return Result.failure("Refresh token cannot be null or empty");
        }
        if (response.scope() == null || response.scope().isEmpty()) {
            return Result.failure("Scope cannot be null or empty");
        }

        return Result.success(null);
    }
}
