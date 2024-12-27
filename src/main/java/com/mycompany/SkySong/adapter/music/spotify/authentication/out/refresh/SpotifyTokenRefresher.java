package com.mycompany.SkySong.adapter.music.spotify.authentication.out.refresh;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
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

    public String refreshAccessToken(int userId) {
        String refreshToken = tokenStore.getRefreshToken(userId);
        SpotifyRefreshTokenRequest request = new SpotifyRefreshTokenRequest("refresh_token", refreshToken);
        validator.validateRequest(request);

        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
        validator.validateResponse(response);

        if (response.refreshToken() != null && !response.refreshToken().isEmpty()) {
            tokenStore.saveRefreshToken(userId, response.refreshToken());
        }

        return response.accessToken();
    }
}
