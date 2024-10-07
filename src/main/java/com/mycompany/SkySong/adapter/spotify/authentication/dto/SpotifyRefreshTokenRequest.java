package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public record SpotifyRefreshTokenRequest(@JsonProperty("grant_type") String grantType,
                                         @JsonProperty("refresh_token") String refreshToken) {

    public SpotifyRefreshTokenRequest {
        if (grantType == null || grantType.isEmpty()) {
            throw new IllegalArgumentException("Grant type cannot be null or empty");
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", List.of(this.grantType));
        formData.put("refresh_token", List.of(this.refreshToken));
        return formData;
    }
}
