package com.mycompany.SkySong.spotify.authentication.adapter.out.authentication.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
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

    public SpotifyTokenResponse sendTokenRequest(MultiValueMap<String, String> bodyData) {
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
                    return Mono.error(new ApiBadRequestException(
                            "There seems to be an issue with the request to Spotify. Please check your input and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST));
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response -> {
                    logger.error("[Spotify API] Unauthorized access - invalid credentials or expired/invalid authorization code",
                            context("status", response.statusCode()));
                    return Mono.error(new ApiAuthenticationException(
                            "Spotify authentication failed. Please try again or reauthorize.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED));
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, response -> {
                    logger.error("[Spotify API] To many requests",
                            context("status", response.statusCode()));
                    return Mono.error(new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Spotify API. Please wait a moment and try again.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT));
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, response -> {
                    logger.error("[Spotify API] Access forbidden",
                            context("status", response.statusCode()));
                    return Mono.error(new ApiForbiddenException(
                            "You are not authorized to access Spotify resources.",
                            ErrorType.EXTERNAL_API_FORBIDDEN));
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, response -> {
                    logger.error("[Spotify API] Service unavailable",
                            context("status", response.statusCode()));
                    return Mono.error(new ApiServerErrorException(
                            "Spotify service is currently unavailable.",
                            ErrorType.EXTERNAL_API_UNAVAILABLE));
                })
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.error("[Spotify API] Unexpected client error",
                            context("status", response.statusCode()));
                    return Mono.error(new ApiClientErrorException(
                            "An unexpected client error occurred while calling Spotify.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logger.error("[Spotify API] Unexpected server error",
                            context("status", response.statusCode()));
                    return Mono.error(new ApiServerErrorException(
                            "An unexpected server error occurred while calling Spotify.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR));
                })

                .bodyToMono(SpotifyTokenResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    logger.error("[Spotify API] Request timed out", ex);
                    return new ApiRequestTimeoutException(
                            "The request to Spotify timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_REQUEST_TIMEOUT);
                })
                .block();
    }
}
