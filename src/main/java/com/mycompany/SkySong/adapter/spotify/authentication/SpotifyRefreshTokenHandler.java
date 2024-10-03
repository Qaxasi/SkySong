package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.api.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyRefreshTokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyRefreshTokenHandler {
    private final JwtTokenManager jwtTokenManager;
    private final SpotifyTokenClient spotifyTokenClient;
    private final RedisTokenStore redisTokenStore;

    public SpotifyRefreshTokenHandler(JwtTokenManager jwtTokenManager,
                                      SpotifyTokenClient spotifyTokenClient,
                                      RedisTokenStore tokenStore) {
        this.jwtTokenManager = jwtTokenManager;
        this.spotifyTokenClient = spotifyTokenClient;
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
        SpotifyTokenResponse response = spotifyTokenClient.sendTokenRequest(formData);

        redisTokenStore.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
