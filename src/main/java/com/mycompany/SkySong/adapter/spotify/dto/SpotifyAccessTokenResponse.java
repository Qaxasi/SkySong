package com.mycompany.SkySong.adapter.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyAccessTokenResponse(@JsonProperty("access_token") String accessToken,
                                         @JsonProperty("token_type") String tokenType,
                                         @JsonProperty("expires_in") int expirationTimeInSec,
                                         @JsonProperty("refreshToken") String refreshToken,
                                         @JsonProperty("scope") String scope) {
}
