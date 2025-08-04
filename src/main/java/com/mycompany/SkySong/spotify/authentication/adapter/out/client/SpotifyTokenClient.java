package com.mycompany.SkySong.spotify.authentication.adapter.out.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyAccessTokenRefreshRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyAuthorizationRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.spotify.config.SpotifyProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.spotify.authentication.adapter.out.client.validator.SpotifyTokenExchangeValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SpotifyTokenClient {
    private final WebClient webClient;
    private final SpotifyProperties properties;
    private final ApplicationLogger logger;
    private final SpotifyTokenExchangeValidator validator;

    public SpotifyTokenClient(@Qualifier("spotifyTokenClient") final WebClient webClient,
                              final SpotifyProperties properties,
                              final ApplicationLogger logger,
                              final SpotifyTokenExchangeValidator validator) {
        this.properties = properties;
        this.webClient = webClient;
        this.logger = logger;
        this.validator = validator;
    }

    public Result<SpotifyTokenResponse> exchangeAuthorizationCode(final String authCode) {
        final SpotifyAuthorizationRequest request =
                new SpotifyAuthorizationRequest("authorization_code", authCode, properties.redirectUri());

        return validator.validateAuthorizationRequest(request)
                .onFailure(error -> logger.warn("Spotify authorization request validation failed",
                        context(Map.of("error", error.errorMessage(), "errorType", error.errorType()))))
                .flatMap(ignored -> {
                    try {
                        final SpotifyTokenResponse response = sendTokenRequest(request.toMultiValueMap());
                        return validator.validateAuthorizationResponse(response)
                                .onFailure(error2 -> logger.warn("Spotify response validation failed",
                                        context(Map.of("error", error2.errorMessage(), "errorType", error2.errorType()))))
                                .map(ignored2 -> response);
                    } catch (ExternalApiException e) {
                        return Result.failure(e.getMessage(), e.getErrorType());
                    }
                });
    }

    public Result<SpotifyTokenResponse> exchangeRefreshToken(final String refreshToken) {
        final SpotifyAccessTokenRefreshRequest request =
                new SpotifyAccessTokenRefreshRequest("refresh_token", refreshToken);

        return validator.validateAccessTokenRefreshRequest(request)
                .onFailure(error -> logger.warn("Spotify access token refresh request validation failed",
                        context(Map.of("error", error.errorMessage(), "errorType", error.errorType()))))
                .flatMap(ignored -> {
                    try {
                        final SpotifyTokenResponse response = sendTokenRequest(request.toMultiValueMap());
                        return validator.validateAccessTokenRefreshResponse(response)
                                .onFailure(error2 -> logger.warn("Spotify response validation failed",
                                        context(Map.of("error", error2.errorMessage(), "errorType", error2.errorType()))))
                                .map(ignored2 -> response);
                    } catch (ExternalApiException e) {
                        return Result.failure(e.getMessage(), e.getErrorType());
                    }
                });
    }

    private SpotifyTokenResponse sendTokenRequest(final MultiValueMap<String, String> bodyData) {
        return webClient.post()
                .headers(headers -> {
                    headers.setBasicAuth(properties.clientId(), properties.clientSecret());
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .bodyValue(bodyData)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response -> {
                    logger.warn("[Spotify API] Request was malformed or contained invalid parameters",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "There seems to be an issue with the request to Spotify. Please check your input and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST));
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response -> {
                    logger.error("[Spotify API] Unauthorized access - invalid credentials or expired/invalid authorization code",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "Spotify authentication failed. Please try again or reauthorize.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED));
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, response -> {
                    logger.error("[Spotify API] To many requests",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "Exceeded number of allowed calls to Spotify API. Please wait a moment and try again.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT));
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, response -> {
                    logger.error("[Spotify API] Access forbidden",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "You are not authorized to access Spotify resources.",
                            ErrorType.EXTERNAL_API_FORBIDDEN));
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, response -> {
                    logger.error("[Spotify API] Service unavailable",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "Spotify service is currently unavailable.",
                            ErrorType.EXTERNAL_API_SERVICE_UNAVAILABLE));
                })
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.error("[Spotify API] Unexpected client error",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "An unexpected client error occurred while calling Spotify.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logger.error("[Spotify API] Unexpected server error",
                            context("status", response.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "An unexpected server error occurred while calling Spotify.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR));
                })

                .bodyToMono(SpotifyTokenResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    logger.error("[Spotify API] Request timed out", ex);
                    return new ExternalApiTimeoutException(
                            "The request to Spotify timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_REQUEST_TIMEOUT);
                })
                .block();
    }
}
