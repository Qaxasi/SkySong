package com.mycompany.SkySong.adapter.music.spotify.authentication.out.access;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.domain.music.authentication.port.MusicServiceAuthenticator;
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

        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri);

        return validateRequest(request)
                .flatMap(ignored -> callSpotifyApi(request))
                .flatMap(response -> validateAndStoreRefreshToken(response, userId));
    }

    private Result<Void> validateRequest(SpotifyAccessTokenRequest request) {
        return validator.validateRequest(request);
    }

    private Result<SpotifyTokenResponse> callSpotifyApi(SpotifyAccessTokenRequest request) {
        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
        return Result.success(response);
    }

    private Result<String> validateAndStoreRefreshToken(SpotifyTokenResponse response, int userId) {
        return validator.validateResponse(response)
                .map(ignored -> {
                    tokenStore.saveRefreshToken(userId, response.refreshToken());
                    return response.accessToken();
                });

    }
}
