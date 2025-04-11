package com.mycompany.SkySong.adapter.music.spotify.authentication.out.access;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.domain.music.authentication.port.MusicServiceAuthenticator;
import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotifyAuthenticator implements MusicServiceAuthenticator {
    private final SpotifyTokenClient api;
    private final SpotifyAccessTokenValidator validator;
    private final String redirectUri;
    private final RedisTokenStore tokenStore;

    public SpotifyAuthenticator(SpotifyTokenClient api,
                                SpotifyAccessTokenValidator validator,
                                @Value("${redirect.uri}") String redirectUri,
                                RedisTokenStore tokenStore) {
        this.api = api;
        this.validator = validator;
        this.redirectUri = redirectUri;
        this.tokenStore = tokenStore;
    }

    @Override
    public Result<String> authenticateAndReturnToken(int userId, AuthParams params) {
        String authCode = params.authCode();

        log.info("Authenticating user {} via Spotify using provided authorization code", userId);

        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri);

        return validator.validateRequest(request)
                .flatMap(ignored -> callSpotifyApi(request, userId))
                .flatMap(response -> validateResponseAndReturnAccessToken(response, userId));
    }

    private Result<SpotifyTokenResponse> callSpotifyApi(SpotifyAccessTokenRequest request, int userId) {
        try {
            SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
            log.info("Spotify access token successfully received for user: {}", userId);
            return Result.success(response);
        } catch (BaseApiException e) {
            log.warn("Spotify token request failed for user: {}", userId);
            return Result.failure(e.getMessage(), e.getErrorType());
        }
    }

    private Result<String> validateResponseAndReturnAccessToken(SpotifyTokenResponse response, int userId) {
        return validator.validateResponse(response)
                .map(ignored -> storeRefreshAndReturnAccessToken(response, userId));
    }

    private String storeRefreshAndReturnAccessToken(SpotifyTokenResponse response, int userId) {
        tokenStore.saveRefreshToken(userId, response.refreshToken());
        log.debug("Refresh token for user: {} successfully stored", userId);
        return response.accessToken();
    }
}
