package com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record SpotifyAuthorizationRequest(@JsonProperty("grant_type") String grantType,
                                          @JsonProperty("code") String authCode,
                                          @JsonProperty("redirect_uri") String redirectUri) {
    
    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", this.grantType);
        map.add("code", this.authCode);
        map.add("redirect_uri", this.redirectUri);
        return map;
    }
}
