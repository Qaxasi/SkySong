package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.shared.utils.Result;

public class SpotifyRefreshTokenValidator {
    public Result<Void> validateRequest(SpotifyRefreshTokenRequest request) {
        if (request.grantType() == null || request.grantType().isEmpty()) {
            Result.failure("Grant type cannot be null or empty");
        }
        if (request.refreshToken() == null || request.refreshToken().isEmpty()) {
            Result.failure("Refresh token cannot be null or empty");
        }
        return Result.success(null);
    }
}
