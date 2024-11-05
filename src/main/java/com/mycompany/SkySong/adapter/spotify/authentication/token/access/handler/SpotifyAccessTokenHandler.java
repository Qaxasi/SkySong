package com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.validation.SpotifyAccessTokenValidator;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import com.mycompany.SkySong.adapter.spotify.authentication.api.SpotifyTokenApi;
import com.mycompany.SkySong.adapter.spotify.authentication.store.RedisTokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyAccessTokenHandler {
    private final String redirectUri;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTokenStore redisTokenStore;
    private final SpotifyTokenApi spotifyTokenApi;
    private final SpotifyAccessTokenValidator validator;

    public SpotifyAccessTokenHandler(@Value("${REDIRECT_URI}") String redirectUri,
                                     JwtTokenManager jwtTokenManager,
                                     RedisTokenStore redisTokenStore,
                                     SpotifyTokenApi spotifyTokenApi,
                                     SpotifyAccessTokenValidator validator) {
        this.redirectUri = redirectUri;
        this.jwtTokenManager = jwtTokenManager;
        this.redisTokenStore = redisTokenStore;
        this.spotifyTokenApi = spotifyTokenApi;
        this.validator = validator;
    }

    public Result<String> retrieveSpotifyAccessToken(String authCode, String jwtToken) {
        if (jwtToken == null || jwtToken.isBlank()) {
            log.error("JWT token is null or empty");
            return Result.failure("Jwt token is missing or invalid", ErrorType.BAD_REQUEST);
        }

        return jwtTokenManager.extractUserId(jwtToken)
                .flatMap(userId -> fetchToken(authCode, userId));
    }

    private Result<String> fetchToken(String authCode, int userId) {
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri);

        return validator.validateRequest(request)
                .flatMap(validationPassed -> spotifyTokenApi.sendAccessTokenRequest(request))
                .flatMap(response -> {
                    redisTokenStore.saveRefreshToken(userId, response.refreshToken());
                    return Result.success(response.accessToken());
                });
    }
}
