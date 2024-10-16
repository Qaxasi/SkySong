package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record SpotifyRefreshTokenRequest(@JsonProperty("grant_type") String grantType,
                                         @JsonProperty("refresh_token") String refreshToken) {
    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", this.grantType);
        formData.add("refresh_token", this.refreshToken);
        return formData;
    }
}
