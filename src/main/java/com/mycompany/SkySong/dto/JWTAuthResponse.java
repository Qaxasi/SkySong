package com.mycompany.SkySong.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JWTAuthResponse(@JsonProperty("accessToken") String accessToken,
                              @JsonProperty("tokenType") String tokenType) {
}
