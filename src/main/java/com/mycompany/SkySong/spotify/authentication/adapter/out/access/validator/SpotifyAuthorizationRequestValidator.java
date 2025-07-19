package com.mycompany.SkySong.spotify.authentication.adapter.out.validator;

import com.mycompany.SkySong.spotify.authentication.adapter.out.dto.SpotifyAuthorizationRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.spotify.authentication.adapter.out.exception.SpotifyInvalidRequestException;
import com.mycompany.SkySong.spotify.authentication.adapter.out.exception.SpotifyInvalidResponseException;
import org.springframework.stereotype.Component;

@Component
public class SpotifyAuthorizationRequestValidator {
    public void validateRequest(final SpotifyAuthorizationRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            throw new SpotifyInvalidRequestException("Grant type cannot be null or empty");
        }
        if (request.authCode() == null || request.authCode().isBlank()) {
            throw new SpotifyInvalidRequestException("Authorization code cannot be null or empty");
        }
        if (request.redirectUri() == null || request.redirectUri().isBlank()) {
            throw new SpotifyInvalidRequestException("Redirect uri cannot be null or empty");
        }
    }

    public void validateResponse(final SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            throw new SpotifyInvalidResponseException("Access token cannot be null or empty");
        }
        if (response.refreshToken() == null || response.refreshToken().isBlank()) {
            throw new SpotifyInvalidResponseException("Refresh token cannot be null or empty");
        }
    }
}
