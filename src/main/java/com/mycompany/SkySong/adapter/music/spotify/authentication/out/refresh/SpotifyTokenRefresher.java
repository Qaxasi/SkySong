package com.mycompany.SkySong.adapter.music.spotify.authentication.out.refresh;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Service;

@Service
public class SpotifyTokenRefresher {
    private final SpotifyTokenClient api;
    private final SpotifyRefreshTokenValidator validator;
    private final RedisTokenStore tokenStore;

    public SpotifyTokenRefresher(SpotifyTokenClient api,
                                 SpotifyRefreshTokenValidator validator,
                                 RedisTokenStore tokenStore) {
        this.api = api;
        this.validator = validator;
        this.tokenStore = tokenStore;
    }

    public Result<String> refreshAccessToken(int userId) {
       return tokenStore.getRefreshToken(userId)
               .map(token -> new SpotifyRefreshTokenRequest("refresh_token", token))
               .flatMap(request ->
                       validateRequest(request)
                               .flatMap(ignored -> callSpotifyApi(request))
                               .flatMap(response -> validateAndStoreRefreshToken(response, userId)));
    }

    private Result<Void> validateRequest(SpotifyRefreshTokenRequest request) {
        return validator.validateRequest(request);
    }

    private Result<SpotifyTokenResponse> callSpotifyApi(SpotifyRefreshTokenRequest request) {
        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
        return Result.success(response);
    }

    private Result<String> validateAndStoreRefreshToken(SpotifyTokenResponse response, int userId) {
        return validator.validateResponse(response)
                .map(ignored -> {
                    if (response.refreshToken() != null && !response.refreshToken().isBlank()) {
                        tokenStore.saveRefreshToken(userId, response.refreshToken());
                    }
                    return response.accessToken();
                });
    }
}
