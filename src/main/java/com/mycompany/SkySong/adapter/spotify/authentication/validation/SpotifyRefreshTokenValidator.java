package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;

public class SpotifyRefreshTokenValidator {
    public Result<Void> validateRequest(SpotifyRefreshTokenRequest request) {
        if (request.grantType() == null || request.grantType().isEmpty()) {
            Result.failure("Grant type cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        if (request.refreshToken() == null || request.refreshToken().isEmpty()) {
            Result.failure("Refresh token cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        return Result.success(null);
    }

    public Result<Void> validateResponse(SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isEmpty()) {
            return Result.failure("Access token cannot be null or empty", ErrorType.UNPROCESSABLE_ENTITY);
        }
        if (response.scope() == null || response.scope().isEmpty()) {
            return Result.failure("Scope cannot be null or empty", ErrorType.UNPROCESSABLE_ENTITY);
        }

        return Result.success(null);
    }
}
