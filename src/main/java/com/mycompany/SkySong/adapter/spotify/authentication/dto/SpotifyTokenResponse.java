package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyTokenResponse(@JsonProperty("access_token") String accessToken,
                                   @JsonProperty("token_type") String tokenType,
                                   @JsonProperty("expires_in") int expirationTimeInSec,
                                   @JsonProperty("refresh_token") String refreshToken,
                                   @JsonProperty("scope") String scope) {
}
    