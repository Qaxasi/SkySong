package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.SkySong.adapter.spotify.authentication.api.Result;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SpotifyAccessTokenRequest {
    private final String grantType;
    private final String authCode;
    private final String redirectUri;

    private SpotifyAccessTokenRequest(@JsonProperty("grant_type") String grantType,
                                      @JsonProperty("code") String authCode,
                                      @JsonProperty("redirect_uri") String redirectUri) {
        this.grantType = grantType;
        this.authCode = authCode;
        this.redirectUri = redirectUri;
    }

    public static class Builder {
        private String grantType;
        private String authCode;
        private String redirectUri;

        public Builder withGrantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder withAuthCode(String authCode) {
            this.authCode = authCode;
            return this;
        }

        public Builder withRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Result<SpotifyAccessTokenRequest> build() {
            if (grantType == null || grantType.isEmpty()) {
                return Result.failure("Grant type cannot be null or empty");
            }
            if (authCode == null || authCode.isEmpty()) {
                return Result.failure("Authorization code cannot be null or empty");
            }
            if (redirectUri == null || redirectUri.isEmpty()) {
                return Result.failure("Redirect URI cannot be null or empty");
            }
            return Result.success(new SpotifyAccessTokenRequest(grantType, authCode, redirectUri));
        }
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", this.grantType);
        map.add("code", this.authCode);
        map.add("redirect_uri", this.redirectUri);
        return map;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
