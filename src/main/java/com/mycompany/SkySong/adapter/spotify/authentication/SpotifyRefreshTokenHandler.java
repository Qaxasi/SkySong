package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.api.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyRefreshTokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyRefreshTokenHandler {
    private final JwtTokenManager jwtTokenManager;
    private final SpotifyTokenClient spotifyTokenClient;
    private final SpotifyTokenRedisHandler redisHandler;

    public SpotifyRefreshTokenHandler(JwtTokenManager jwtTokenManager,
                                      SpotifyTokenClient spotifyTokenClient,
                                      SpotifyTokenRedisHandler redisHandler) {
        this.jwtTokenManager = jwtTokenManager;
        this.spotifyTokenClient = spotifyTokenClient;
        this.redisHandler = redisHandler;
    }

    public SpotifyAccessTokenResponse refreshSpotifyAccessToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        int userId = jwtTokenManager.extractUserId(jwtToken);

        String refreshToken = redisHandler.getRefreshToken(userId);

        MultiValueMap<String, String> formData = new SpotifyRefreshTokenRequest("refresh_token", refreshToken).toMultiValueMap();
        SpotifyAccessTokenResponse response = spotifyTokenClient.sendTokenRequest(formData);

        if (response.refreshToken() != null && !response.refreshToken().isEmpty()) {
            redisHandler.saveRefreshToken(userId, response.refreshToken());
        }

        return response;
    }
}
