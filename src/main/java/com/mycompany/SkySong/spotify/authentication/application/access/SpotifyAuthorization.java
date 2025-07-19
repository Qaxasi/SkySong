package com.mycompany.SkySong.spotify.authentication.application.access;

import com.mycompany.SkySong.config.spotify.SpotifyProperties;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.SpotifyTokenClient;
import com.mycompany.SkySong.spotify.authentication.adapter.out.access.dto.SpotifyAuthorizationRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.access.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.spotify.authentication.adapter.out.redis.RedisTokenStore;
import com.mycompany.SkySong.spotify.authentication.AuthParams;
import com.mycompany.SkySong.shared.error.BaseApiException;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.spotify.authentication.adapter.out.access.validator.SpotifyAuthorizationRequestValidator;
import org.springframework.stereotype.Service;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Service
public class SpotifyAuthorizationService {
    private final SpotifyTokenClient api;
    private final SpotifyAuthorizationRequestValidator validator;
    private final SpotifyProperties properties;
    private final RedisTokenStore tokenStore;
    private final ApplicationLogger logger;

    public SpotifyAuthorizationService(final SpotifyTokenClient api,
                                       final SpotifyAuthorizationRequestValidator validator,
                                       final SpotifyProperties properties,
                                       final RedisTokenStore tokenStore,
                                       final ApplicationLogger logger) {
        this.api = api;
        this.validator = validator;
        this.properties = properties;
        this.tokenStore = tokenStore;
        this.logger = logger;
    }

    public Result<String> authenticateAndReturnToken(int userId, final AuthParams params) {
        final String authCode = params.authCode();

        logger.info("Authenticating user via Spotify using provided authorization code", context("userId", userId));

        final SpotifyAuthorizationRequest request = new SpotifyAuthorizationRequest(
                "authorization_code", authCode, properties.redirectUri());

        return validateRequestAndCallSpotify(request, userId)
                .flatMap(response -> validateAndHandleTokenResponse(response, userId));
    }

    private Result<SpotifyTokenResponse> validateRequestAndCallSpotify(SpotifyAuthorizationRequest request, int userId) {
        return validator.validateRequest(request)
                .flatMap(ignored -> {
                    try {
                        SpotifyTokenResponse response = api.sendTokenRequest(request.toMultiValueMap());
                        logger.info("Spotify access token successfully received", context("userId", userId));
                        return Result.success(response);
                    } catch (BaseApiException e) {
                        logger.warn("Spotify token request failed", context("userId", userId));
                        return Result.failure(e.getMessage(), e.g);
                    }
                });
    }

    private Result<String> validateAndHandleTokenResponse(SpotifyTokenResponse response, int userId) {
        return validator.validateResponse(response)
                .map(ignored -> {
                    tokenStore.saveRefreshToken(userId, response.refreshToken());
                    logger.info("Spotify access token successfully received and refresh token stored", context("userId", userId));
                        return response.accessToken();
                    });
        }
    }
