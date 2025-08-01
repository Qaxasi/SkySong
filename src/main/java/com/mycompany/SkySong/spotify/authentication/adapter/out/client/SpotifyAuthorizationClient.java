package com.mycompany.SkySong.spotify.authentication.adapter.out.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.spotify.authentication.adapter.out.access.dto.SpotifyAccessTokenRefreshRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.access.dto.SpotifyAuthorizationRequest;
import com.mycompany.SkySong.spotify.authentication.adapter.out.access.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.config.spotify.SpotifyProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Component
public class SpotifyTokenClient {
    private final WebClient webClient;
    private final SpotifyProperties properties;
    private final ApplicationLogger logger;

    public SpotifyTokenClient(@Qualifier("spotifyTokenClient") final WebClient webClient,
                              final SpotifyProperties properties,
                              final ApplicationLogger logger) {
        this.properties = properties;
        this.webClient = webClient;
        this.logger = logger;
    }

    public Result<SpotifyTokenResponse> exchangeAuthorizationCode(final String authCode) {
        final SpotifyAuthorizationRequest request =
                new SpotifyAuthorizationRequest("authorization_code", authCode, properties.redirectUri());

        return validateRequest(request)
                .flatMap(ignored -> executeTokenRequest(request.toMultiValueMap(), this::validateAccessTokenResponse));
    }

    public Result<SpotifyTokenResponse> exchangeRefreshToken(final String refreshToken) {
        final SpotifyAccessTokenRefreshRequest request =
                new SpotifyAccessTokenRefreshRequest("refresh_token", refreshToken);

        return validateRequest(request)
                .flatMap(ignored -> executeTokenRequest(request.toMultiValueMap(), this::validateAccessTokenRefreshResponse));
    }

    private Result<SpotifyTokenResponse> executeTokenRequest(final MultiValueMap<String, String> bodyData,
                                                             final Consumer<SpotifyTokenResponse> responseValidator) {
        try {
            final SpotifyTokenResponse response = sendTokenRequest(bodyData);
            responseValidator.accept(response);
            return Result.success(response);
        } catch (ExternalApiResponseException e) {
            logger.warn("[Spotify API] Invalid spotify response", context("errorType", e.getErrorType()));
            return Result.failure(e.getMessage(), e.getErrorType());
        } catch (ExternalApiException e) {
            return Result.failure(e.getMessage(), e.getErrorType());
        }
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

    private Result<Void> validateAccessTokenResponse(final SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            throw new ExternalApiResponseException("Access token cannot be null or empty", ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
        if (response.refreshToken() == null || response.refreshToken().isBlank()) {
            throw new ExternalApiResponseException("Refresh token cannot be null or empty", ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
    }

    private Result<Void> validateAccessTokenRefreshResponse(final SpotifyTokenResponse response) {
        if (response.accessToken() == null || response.accessToken().isBlank()) {
            throw new ExternalApiResponseException("Access token cannot be null or empty", ErrorType.SPOTIFY_INVALID_RESPONSE);
        }
    }

    private Result<Void> validateRequest(final SpotifyAccessTokenRefreshRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            return Result.failure("Grant type cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            return Result.failure("Refresh token cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        return Result.success();
    }

    private Result<Void> validateRequest(final SpotifyAuthorizationRequest request) {
        if (request.grantType() == null || request.grantType().isBlank()) {
            return Result.failure("Grant type cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        if (request.authCode() == null || request.authCode().isBlank()) {
            return Result.failure("Authorization code cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        if (request.redirectUri() == null || request.redirectUri().isBlank()) {
            return Result.failure("Redirect uri cannot be null or empty", ErrorType.SPOTIFY_INVALID_REQUEST);
        }
        return Result.success();
    }
}
