package com.mycompany.SkySong.adapter.music.spotify.authentication.out.access;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyAuthorizationRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyAuthorizationService {
    private final SpotifyTokenClient api;
    private final SpotifyAuthorizationRequestValidator validator;
    private final String redirectUri;
    private final RedisTokenStore tokenStore;

    public SpotifyAuthorizationService(SpotifyTokenClient api,
                                       SpotifyAuthorizationRequestValidator validator,
                                       @Value("${redirect.uri}") String redirectUri,
                                       RedisTokenStore tokenStore) {
        this.api = api;
        this.validator = validator;
        this.redirectUri = redirectUri;
        this.tokenStore = tokenStore;
    }

    public Result<String> authenticateAndReturnToken(int userId, AuthParams params) {
        String authCode = params.authCode();

        log.info("Authenticating user {} via Spotify using provided authorization code", userId);

        SpotifyAuthorizationRequest request = new SpotifyAuthorizationRequest(
                "authorization_code", authCode, redirectUri);

        return validateRequestAndCallSpotify(request, userId)
                .flatMap(response -> validateAndHandleTokenResponse(response, userId));
    }

    private Result<SpotifyTokenResponse> validateRequestAndCallSpotify(SpotifyAuthorizationRequest request, int userId) {
        return validator.validateRequest(request)
                .flatMap(ignored -> {
                    try {
                        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
                        log.info("Spotify access token successfully received for user: {}", userId);
                        return Result.success(response);
                    } catch (BaseApiException e) {
                        log.warn("Spotify token request failed for user: {}", userId);
                        return Result.failure(e.getMessage(), e.getErrorType());
                    }
                });
    }

    private Result<String> validateAndHandleTokenResponse(SpotifyTokenResponse response, int userId) {
        return validator.validateResponse(response)
                .map(ignored -> {
                    tokenStore.saveRefreshToken(userId, response.refreshToken());
                    log.info("Spotify access token successfully received and refresh token stored for user: {}", userId);
                    return response.accessToken();
                });
    }
}
