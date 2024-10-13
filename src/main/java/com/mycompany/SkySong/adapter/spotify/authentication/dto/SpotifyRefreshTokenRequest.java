package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.SkySong.shared.utils.Result;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class SpotifyRefreshTokenRequest {

    private final String grantType;
    private final String refreshToken;

    private SpotifyRefreshTokenRequest(@JsonProperty("grant_type") String grantType,
                                       @JsonProperty("refresh_token") String refreshToken) {
        this.grantType = grantType;
        this.refreshToken = refreshToken;
    }

    public static class Builder {
        private String grantType;
        private String refreshToken;

        public Builder withGrantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder withRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Result<SpotifyRefreshTokenRequest> build() {
            if (grantType == null || grantType.isEmpty()) {
                return Result.failure("Grant type cannot be null or empty");
            }
            if (refreshToken == null || refreshToken.isEmpty()) {
                return Result.failure("Refresh Token cannot be null or empty");
            }
            return Result.success(new SpotifyRefreshTokenRequest(grantType, refreshToken));
        }
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", this.grantType);
        formData.add("refresh_token", this.refreshToken);
        return formData;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
