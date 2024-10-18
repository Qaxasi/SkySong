package com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.validation.SpotifyAccessTokenValidator;
import com.mycompany.SkySong.shared.utils.Result;
import com.mycompany.SkySong.adapter.spotify.authentication.api.SpotifyTokenApi;
import com.mycompany.SkySong.adapter.spotify.authentication.store.RedisTokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SpotifyAccessTokenHandler {
    private final String redirectUri;
    private final JwtTokenManager jwtTokenManager;
    private final RedisTokenStore redisTokenStore;
    private final SpotifyTokenApi spotifyTokenApi;
    private final SpotifyAccessTokenValidator tokenValidator;

    public SpotifyAccessTokenHandler(@Value("${REDIRECT_URI}") String redirectUri,
                                     JwtTokenManager jwtTokenManager,
                                     RedisTokenStore redisTokenStore,
                                     SpotifyTokenApi spotifyTokenApi,
                                     SpotifyAccessTokenValidator tokenValidator) {
        this.redirectUri = redirectUri;
        this.jwtTokenManager = jwtTokenManager;
        this.redisTokenStore = redisTokenStore;
        this.spotifyTokenApi = spotifyTokenApi;
        this.tokenValidator = tokenValidator;
    }

    public Result<String> retrieveSpotifyAccessToken(String authCode, String jwtToken) {
        return jwtTokenManager.extractAndValidateUserId(jwtToken)
                .flatMap(userId -> {
                    SpotifyAccessTokenRequest accessTokenRequest = new SpotifyAccessTokenRequest(
                            "authorization_code", authCode, redirectUri);
                    Result<Void> validationResult = tokenValidator.validateRequest(accessTokenRequest);
                    if (!validationResult.success()) {
                        return Result.failure(validationResult.errorMessage());
                    }

                    Result<SpotifyTokenResponse> responseResult = spotifyTokenApi.sendAccessTokenRequest(accessTokenRequest);
                    if (!responseResult.success()) {
                        return Result.failure(responseResult.errorMessage());
                    }

                    redisTokenStore.saveRefreshToken(userId, responseResult.data().refreshToken());
                    return Result.success(responseResult.data().accessToken());

                });
    }
}
