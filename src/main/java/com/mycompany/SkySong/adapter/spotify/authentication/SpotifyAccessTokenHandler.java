package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.api.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyAccessTokenHandler {
    private final String redirectUri;
    private final JwtTokenManager jwtTokenManager;
    private final SpotifyTokenRedisHandler redisHandler;
    private final SpotifyTokenClient spotifyTokenClient;

    public SpotifyAccessTokenHandler(@Value("${REDIRECT_URI}") String redirectUri,
                                     JwtTokenManager jwtTokenManager,
                                     SpotifyTokenRedisHandler redisHandler,
                                     SpotifyTokenClient spotifyTokenClient) {

        if (redirectUri == null || redirectUri.isEmpty()) {
            throw new IllegalArgumentException("Redirect uri is missing or invalid");
        }
        this.redirectUri = redirectUri;
        this.jwtTokenManager = jwtTokenManager;
        this.redisHandler = redisHandler;
        this.spotifyTokenClient = spotifyTokenClient;
    }

    public String retrieveSpotifyAccessToken(String authCode, String jwtToken) {
        if (authCode == null || authCode.isEmpty()) {
            throw new IllegalArgumentException("Authorization code is missing or invalid");
        }
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        int userId = jwtTokenManager.extractUserId(jwtToken);

        MultiValueMap<String, String> formData = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri).toMultiValueMap();
        SpotifyAccessTokenResponse response = spotifyTokenClient.sendTokenRequest(formData);

        redisHandler.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
