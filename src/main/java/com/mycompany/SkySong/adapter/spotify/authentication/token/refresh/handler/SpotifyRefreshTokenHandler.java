package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.api.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.spotify.authentication.repository.RedisTokenRepository;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SpotifyRefreshTokenHandler {
    private final JwtTokenManager jwtTokenManager;
    private final SpotifyTokenClient spotifyTokenClient;
    private final RedisTokenRepository redisTokenRepository;

    public SpotifyRefreshTokenHandler(JwtTokenManager jwtTokenManager,
                                      SpotifyTokenClient spotifyTokenClient,
                                      RedisTokenRepository tokenStore) {
        this.jwtTokenManager = jwtTokenManager;
        this.spotifyTokenClient = spotifyTokenClient;
        this.redisTokenRepository = tokenStore;
    }

    public String refreshSpotifyAccessToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        int userId = jwtTokenManager.extractUserId(jwtToken);

        String refreshToken = redisTokenRepository.getRefreshToken(userId);

        MultiValueMap<String, String> formData = new SpotifyRefreshTokenRequest(
                "refresh_token", refreshToken).toMultiValueMap();
        SpotifyTokenResponse response = spotifyTokenClient.sendTokenRequest(formData);

        redisTokenRepository.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
