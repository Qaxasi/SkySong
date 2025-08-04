package com.mycompany.SkySong.spotify.authentication.adapter.out.client.validator;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyAccessTokenRefreshRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyAuthorizationRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyTokenResponse;

public class SpotifyTokenExchangeValidator {
    public Result<Void> validateAuthorizationRequest(final SpotifyAuthorizationRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            return Result.failure("Grant type cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        if (request.authCode() == null || request.authCode().isBlank()) {
            return Result.failure("Authorization code cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        if (request.redirectUri() == null || request.redirectUri().isBlank()) {
            return Result.failure("Redirect uri cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        return Result.success();
    }
    public Result<Void> validateAuthorizationResponse(final SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            return Result.failure("Access token cannot be null or empty", ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
        if (response.refreshToken() == null || response.refreshToken().isBlank()) {
            return Result.failure("Refresh token cannot be null or empty", ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
        return Result.success();
    }

    public Result<Void> validateAccessTokenRefreshResponse(final SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            return Result.failure("Access token cannot be null or empty", ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
        return Result.success();
    }

    public Result<Void> validateAccessTokenRefreshRequest(final SpotifyAccessTokenRefreshRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            return Result.failure("Grant type cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            return Result.failure("Refresh token cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        return Result.success();
    }
}
