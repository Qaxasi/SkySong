package com.mycompany.SkySong.music.authorization.AuthorizationController.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyAccessToken (
        @JsonProperty("access_token") String token,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expirationTimeInSec

        ) {
}
