package com.mycompany.SkySong.adapter.spotify.authentication.validation;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyAccessTokenValidator {
    public Result<Void> validateRequest(SpotifyAccessTokenRequest request) {
        if (request.grantType() == null || request.grantType().isEmpty()) {
            log.error("Request validation failed: grant type is null or empty");
            return Result.failure("Grant type cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        if (request.authCode() == null || request.authCode().isEmpty()) {
            log.error("Request validation failed: authorization code is null or empty");
            return Result.failure("Authorization code cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        if (request.redirectUri() == null || request.redirectUri().isEmpty()) {
            log.error("Request validation failed: redirect uri is null or empty");
            return Result.failure("Redirect uri cannot be null or empty", ErrorType.BAD_REQUEST);
        }
        return Result.success(null);
    }

    public Result<Void> validateResponse(SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isEmpty()) {
            log.error("Response validation failed: access token is null or empty");
            return Result.failure("Access token cannot be null or empty",ErrorType.UNPROCESSABLE_ENTITY);
        }
        if (response.refreshToken() == null || response.refreshToken().isEmpty()) {
            log.error("Response validation failed: refresh token is null or empty");
            return Result.failure("Refresh token cannot be null or empty", ErrorType.UNPROCESSABLE_ENTITY);
        }
        if (response.scope() == null || response.scope().isEmpty()) {
            log.error("Response validation failed: scope is null or empty");
            return Result.failure("Scope cannot be null or empty", ErrorType.UNPROCESSABLE_ENTITY);
        }

        return Result.success(null);
    }
}
