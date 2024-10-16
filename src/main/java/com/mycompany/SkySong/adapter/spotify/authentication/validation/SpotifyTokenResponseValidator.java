package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.Result;

public class SpotifyTokenResponseValidator {

    public Result<Void> validateAccessTokenResponse(SpotifyTokenResponse response) {
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

    public Result<Void> validateRefreshTokenResponse(SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isEmpty()) {
            return Result.failure("Access token cannot be null or empty");
        }
        if (response.scope() == null || response.scope().isEmpty()) {
            return Result.failure("Scope cannot be null or empty");
        }

        return Result.success(null);
    }
}
