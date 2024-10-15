package com.mycompany.SkySong.adapter.spotify.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.SkySong.shared.utils.Result;

public record SpotifyAccessTokenResponse(@JsonProperty("access_token") String accessToken,
                                         @JsonProperty("refresh_token") String refreshToken,
                                         @JsonProperty("scope") String scope) {

    public static Result<SpotifyAccessTokenResponse> fromApiResponse(String accessToken,
                                                                     String refreshToken,
                                                                     String scope) {

        if (accessToken == null || accessToken.isEmpty()) {
            return Result.failure("Access token cannot be null or empty.");
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
            return Result.failure("Refresh token cannot be null or empty");
        }
        if (scope == null || scope.isEmpty()) {
            return Result.failure("Scope cannot be null or empty");
        }
        return Result.success(new SpotifyAccessTokenResponse(accessToken, refreshToken, scope));
     }
}
    