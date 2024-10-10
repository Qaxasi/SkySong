package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyTokenResponse(@JsonProperty("access_token") String accessToken,
                                   @JsonProperty("refresh_token") String refreshToken,
                                   @JsonProperty("scope") String scope) {

    public SpotifyTokenResponse {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token cannot be null or empty");
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }
        if (scope == null || scope.isEmpty()) {
            throw new IllegalArgumentException("Scope cannot be null or empty");
        }
     }
}
    