package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public record SpotifyAccessTokenRequest(@JsonProperty("grant_type") String grantType,
                                        @JsonProperty("code") String authCode,
                                        @JsonProperty("redirect_uri") String redirectUri) {

    public SpotifyAccessTokenRequest {
        if (grantType == null || grantType.isEmpty()) {
            throw new IllegalArgumentException("Grant type cannot be null or empty");
        }
        if (authCode == null || authCode.isEmpty()) {
            throw new IllegalArgumentException("Authorization code annot be null or empty");
        }
        if (redirectUri == null || redirectUri.isEmpty()) {
            throw new IllegalArgumentException("Redirect uri cannot be null or empty");
         }
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", List.of(this.grantType));
        formData.put("code", List.of(this.authCode));
        formData.put("redirect_uri", List.of(this.redirectUri));
        return formData;
    }
}
