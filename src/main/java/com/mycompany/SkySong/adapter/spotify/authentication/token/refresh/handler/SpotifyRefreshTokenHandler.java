package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.api.SpotifyTokenApi;
import com.mycompany.SkySong.adapter.spotify.authentication.repository.RedisTokenStore;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyRefreshTokenHandler {
    private final JwtTokenManager jwtTokenManager;
    private final SpotifyTokenApi spotifyTokenApi;
    private final RedisTokenStore redisTokenStore;

    public SpotifyRefreshTokenHandler(JwtTokenManager jwtTokenManager,
                                      SpotifyTokenApi spotifyTokenApi,
                                      RedisTokenStore tokenStore) {
        this.jwtTokenManager = jwtTokenManager;
        this.spotifyTokenApi = spotifyTokenApi;
        this.redisTokenStore = tokenStore;
    }

    public String refreshSpotifyAccessToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        int userId = jwtTokenManager.extractUserId(jwtToken);

        String refreshToken = redisTokenStore.getRefreshToken(userId);

        MultiValueMap<String, String> formData = new SpotifyRefreshTokenRequest(
                "refresh_token", refreshToken).toMultiValueMap();
        SpotifyTokenResponse response = spotifyTokenApi.sendTokenRequest(formData);

        if (response.refreshToken() != null && !response.refreshToken().isEmpty()) {
            redisTokenStore.saveRefreshToken(userId, response.refreshToken());
        }

        return response.accessToken();
    }
}
