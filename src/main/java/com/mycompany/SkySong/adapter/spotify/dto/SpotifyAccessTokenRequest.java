package com.mycompany.SkySong.adapter.spotify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public record SpotifyAccessTokenRequest(@JsonProperty("grant_type") String grantType,
                                        @JsonProperty("code") String code,
                                        @JsonProperty("redirect_uri") String redirectUri) {

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", List.of(this.grantType));
        formData.put("code", List.of(this.code));
        formData.put("redirect_uri", List.of(this.redirectUri));
        return formData;
    }
}
