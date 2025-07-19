package com.mycompany.SkySong.spotify.authentication.adapter.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyTokenResponse(@JsonProperty("access_token") String accessToken,
                                   @JsonProperty("refresh_token") String refreshToken) {}
