package com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.api.SpotifyTokenApi;
import com.mycompany.SkySong.adapter.spotify.authentication.store.RedisTokenStore;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyAccessTokenHandler {
    private final String redirectUri;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTokenStore redisTokenStore;
    private final SpotifyTokenApi spotifyTokenApi;

    public SpotifyAccessTokenHandler(@Value("${REDIRECT_URI}") String redirectUri,
                                     JwtTokenManager jwtTokenManager,
                                     RedisTokenStore redisTokenStore,
                                     SpotifyTokenApi spotifyTokenApi) {
        this.redirectUri = redirectUri;
        this.jwtTokenManager = jwtTokenManager;
        this.redisTokenStore = redisTokenStore;
        this.spotifyTokenApi = spotifyTokenApi;
    }

    public String retrieveSpotifyAccessToken(String authCode, String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        MultiValueMap<String, String> formData = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri).toMultiValueMap();
        SpotifyTokenResponse response = spotifyTokenApi.sendTokenRequest(formData);

        int userId = jwtTokenManager.extractUserId(jwtToken);
        redisTokenStore.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
