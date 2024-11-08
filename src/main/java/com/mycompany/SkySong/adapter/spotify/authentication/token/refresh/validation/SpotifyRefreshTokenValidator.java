package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyRefreshTokenValidator {
    public Result<Void> validateRequest(SpotifyRefreshTokenRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            log.error("Request validation failed: grant type is null or empty");
            return Result.failure("Grant type cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            log.error("Request validation failed: refresh token is null or empty");
            return Result.failure("Refresh token cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        return Result.success(null);
    }

    public Result<Void> validateRefreshTokenResponse(SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            log.error("Response validation failed: access token is null or empty");
            return Result.failure("Access token cannot be null or empty", ErrorType.UNPROCESSABLE_ENTITY);
        }
        if (response.scope() == null || response.scope().isBlank()) {
            log.error("Response validation failed: scope is null or empty");
            return Result.failure("Scope cannot be null or empty", ErrorType.UNPROCESSABLE_ENTITY);
        }

        return Result.success(null);
    }
}
