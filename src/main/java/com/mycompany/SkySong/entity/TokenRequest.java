package com.mycompany.SkySong.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public record TokenRequest(@JsonProperty("grant_type") String grantType,
                           @JsonProperty("code") String code,
                           @JsonProperty("redirect_uri") String redirectUri) {

    public MultiValueMap<String, String> toMultiValueMap() {
        var bodyValue = new LinkedMultiValueMap<String, String>();
        bodyValue.put("grant_type", List.of(this.grantType));
        bodyValue.put("code", List.of(this.code));
        bodyValue.put("redirect_uri", List.of(this.redirectUri));
        return bodyValue;
    }
}
