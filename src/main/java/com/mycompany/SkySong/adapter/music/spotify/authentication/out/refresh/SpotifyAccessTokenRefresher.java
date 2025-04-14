package com.mycompany.SkySong.adapter.music.spotify.authentication.out.refresh;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyAccessTokenRefreshRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyAccessTokenRefresher {
    private final SpotifyTokenClient api;
    private final SpotifyAccessTokenRefreshValidator validator;
    private final RedisTokenStore tokenStore;

    public SpotifyAccessTokenRefresher(SpotifyTokenClient api,
                                       SpotifyAccessTokenRefreshValidator validator,
                                       RedisTokenStore tokenStore) {
        this.api = api;
        this.validator = validator;
        this.tokenStore = tokenStore;
    }

    public Result<String> refreshAccessToken(int userId) {
       return tokenStore.getRefreshToken(userId)
               .map(token -> new SpotifyAccessTokenRefreshRequest("refresh_token", token))
               .flatMap(this::validateRequestAndCallSpotify)
               .flatMap(response -> validateAndHandleTokenResponse(response, userId));
    }

    private Result<SpotifyTokenResponse> validateRequestAndCallSpotify(SpotifyAccessTokenRefreshRequest request) {
        return validator.validateRequest(request)
                .map(ignored -> api.sendTokenRequest(request.toMultiValueMap()));
    }

    private Result<String> validateAndHandleTokenResponse(SpotifyTokenResponse response, int userId) {
        return validator.validateResponse(response)
                .map(ignored -> {
                    if (response.refreshToken() != null && !response.refreshToken().isBlank()) {
                        log.debug("New refresh token received from Spotify for user id: {}", userId);
                        tokenStore.saveRefreshToken(userId, response.refreshToken());
                    }
                    log.info("Spotify access token successfully refreshed for user id: {}", userId);
                    return response.accessToken();
                });
    }
}
