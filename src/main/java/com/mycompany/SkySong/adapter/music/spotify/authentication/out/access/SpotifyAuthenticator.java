package com.mycompany.SkySong.adapter.music.spotify.authentication.out.access;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.persistence.redis.RedisTokenStore;
import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.domain.music.authentication.port.MusicServiceAuthenticator;
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
    public String authenticateAndReturnToken(int userId, AuthParams params) {
        String authCode = params.authCode();
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", authCode, redirectUri);
        accessTokenValidator.validateRequest(request);

        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
        accessTokenValidator.validateResponse(response);

        tokenStore.saveRefreshToken(userId, response.refreshToken());

        return response.accessToken();
    }
}
