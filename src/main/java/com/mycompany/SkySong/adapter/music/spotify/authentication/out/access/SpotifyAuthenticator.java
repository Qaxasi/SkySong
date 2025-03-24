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
    private final SpotifyAccessTokenValidator accessTokenValidator;
    private final String redirectUri;
    private final RedisTokenStore tokenStore;

    public SpotifyAuthenticator(SpotifyTokenClient api,
                                SpotifyAccessTokenValidator accessTokenValidator,
                                @Value("${redirect.uri}") String redirectUri,
                                RedisTokenStore tokenStore) {
        this.api = api;
        this.accessTokenValidator = accessTokenValidator;
        this.redirectUri = redirectUri;
        this.tokenStore = tokenStore;
    }

    @Override
    public Result<String> authenticateAndReturnToken(int userId, AuthParams params) {
        String authCode = params.authCode();
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri);

        Result<Void> requestValidation = accessTokenValidator.validateRequest(request);
        if (requestValidation.isFailure()) {
            return Result.failure(requestValidation.errorMessage(), requestValidation.errorType());
        }

        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());

        Result<Void> responseValidation = accessTokenValidator.validateResponse(response);
        if (responseValidation.isFailure()) {
            return Result.failure(responseValidation.errorMessage(), requestValidation.errorType());
        }

        tokenStore.saveRefreshToken(userId, response.refreshToken());

        return Result.success(response.accessToken());
    }
}
