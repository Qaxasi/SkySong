package com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.api.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.spotify.authentication.repository.RedisTokenRepository;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyAccessTokenHandler {
    private final String redirectUri;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTokenRepository redisTokenRepository;
    private final SpotifyTokenClient spotifyTokenClient;

    public SpotifyAccessTokenHandler(@Value("${REDIRECT_URI}") String redirectUri,
                                     JwtTokenManager jwtTokenManager,
                                     RedisTokenRepository redisTokenRepository,
                                     SpotifyTokenClient spotifyTokenClient) {

        if (redirectUri == null || redirectUri.isEmpty()) {
            throw new IllegalArgumentException("Redirect uri is missing or invalid");
        }
        this.redirectUri = redirectUri;
        this.jwtTokenManager = jwtTokenManager;
        this.redisTokenRepository = redisTokenRepository;
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
        redisTokenRepository.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
