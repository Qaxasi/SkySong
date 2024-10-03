package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.api.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyAccessTokenHandler {
    private final String redirectUri;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTokenStore redisTokenStore;
    private final SpotifyTokenClient spotifyTokenClient;

    public SpotifyAccessTokenHandler(@Value("${REDIRECT_URI}") String redirectUri,
                                     JwtTokenManager jwtTokenManager,
                                     RedisTokenStore redisTokenStore,
                                     SpotifyTokenClient spotifyTokenClient) {

        if (redirectUri == null || redirectUri.isEmpty()) {
            throw new IllegalArgumentException("Redirect uri is missing or invalid");
        }
        this.redirectUri = redirectUri;
        this.jwtTokenManager = jwtTokenManager;
        this.redisTokenStore = redisTokenStore;
        this.spotifyTokenClient = spotifyTokenClient;
    }

    public String retrieveSpotifyAccessToken(String authCode, String jwtToken) {
        if (authCode == null || authCode.isEmpty()) {
            throw new IllegalArgumentException("Authorization code is missing or invalid");
        }
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        MultiValueMap<String, String> formData = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri).toMultiValueMap();
        SpotifyTokenResponse response = spotifyTokenClient.sendTokenRequest(formData);

        int userId = jwtTokenManager.extractUserId(jwtToken);
        redisTokenStore.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
