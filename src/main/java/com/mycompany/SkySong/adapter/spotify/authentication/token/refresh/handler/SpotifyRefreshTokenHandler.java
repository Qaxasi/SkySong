package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.store.RedisTokenStore;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.api.SpotifyRefreshTokenApi;
import com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.validation.SpotifyRefreshTokenValidator;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class SpotifyRefreshTokenHandler {
    private final JwtTokenManager jwtTokenManager;
    private final SpotifyRefreshTokenApi tokenApi;
    private final RedisTokenStore redisTokenStore;
    private final SpotifyRefreshTokenValidator validator;

    public SpotifyRefreshTokenHandler(JwtTokenManager jwtTokenManager,
                                      SpotifyRefreshTokenApi tokenApi,
                                      RedisTokenStore tokenStore,
                                      SpotifyRefreshTokenValidator validator) {
        this.jwtTokenManager = jwtTokenManager;
        this.tokenApi = tokenApi;
        this.redisTokenStore = tokenStore;
        this.validator = validator;
    }

    public Result<String> refreshSpotifyAccessToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            return Result.failure("Jwt token is null or empty", ErrorType.BAD_REQUEST);
        }

        return jwtTokenManager.extractUserId(jwtToken)
                .flatMap(this::refreshToken);
    }

    private Result<String> refreshToken(int userId) {
        return redisTokenStore.getRefreshToken(userId)
                .flatMap(refreshToken -> {
                    SpotifyRefreshTokenRequest request = new SpotifyRefreshTokenRequest("refresh_token", refreshToken);
                    return validator.validateRequest(request)
                            .flatMap(validationPassed -> tokenApi.fetchRefreshToken(request))
                            .flatMap(response -> {
                            if (response.refreshToken() != null && !response.refreshToken().isEmpty()) {
                                redisTokenStore.saveRefreshToken(userId, response.refreshToken());
                            }
                            return Result.success(response.accessToken());
                            });
                });
    }
}
