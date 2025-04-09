package com.mycompany.SkySong.adapter.music.spotify.authentication.out.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class SpotifyTokenClient {
    private final String spotifyClientId;
    private final String spotifyClientSecret;
    private final WebClient webClient;

    public SpotifyTokenClient(@Value("${spotify.client.id}") String spotifyClientId,
                              @Value("${spotify.client.secret}") String spotifyClientSecret,
                              @Qualifier("spotifyTokenClient") WebClient webClient) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.webClient = webClient;
    }

    public SpotifyTokenResponse sendTokenRequest(MultiValueMap<String, String> bodyData) {
        return webClient.post()
                .headers(headers -> {
                    headers.setBasicAuth(spotifyClientId, spotifyClientSecret);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .bodyValue(bodyData)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response -> {
                    log.warn("[Spotify API] Request was malformed or contained invalid parameters - status: {}", response.statusCode());
                    return Mono.error(new ApiBadRequestException(
                            "There seems to be an issue with the request to Spotify. Please check your input and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST));
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response -> {
                    log.error("[Spotify API] Unauthorized access - invalid credentials or expired/invalid authorization code - status: {}", response.statusCode());
                    return Mono.error(new ApiAuthenticationException(
                            "Spotify authentication failed. Please try again or reauthorize.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED));
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, response -> {
                    log.error("[Spotify API] To many requests - status: {}", response.statusCode());
                    return Mono.error(new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Spotify API. Please wait a moment and try again.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT));
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, response -> {
                    log.error("[Spotify API] Access forbidden - status: {}", response.statusCode());
                    return Mono.error(new ApiForbiddenException(
                            "You are not authorized to access Spotify resources.",
                            ErrorType.EXTERNAL_API_FORBIDDEN));
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, response -> {
                    log.error("[Spotify API] Service unavailable - status: {}", response.statusCode());
                    return Mono.error(new ApiServerErrorException(
                            "Spotify service is currently unavailable.",
                            ErrorType.EXTERNAL_API_UNAVAILABLE));
                })
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.error("[Spotify API] Unexpected client error - status: {}", response.statusCode());
                    return Mono.error(new ApiClientErrorException(
                            "An unexpected client error occurred while calling Spotify.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("[Spotify API] Unexpected server error - status: {}", response.statusCode());
                    return Mono.error(new ApiServerErrorException(
                            "An unexpected server error occurred while calling Spotify.",
                            ErrorType.API_SERVER_ERROR));
                })

                .bodyToMono(SpotifyTokenResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    log.error("[Spotify API] Request timed out", ex);
                    return new ApiRequestTimeoutException(
                            "The request to Spotify timed out. Please check your connection and try again.",
                            ErrorType.API_REQUEST_TIMEOUT);
                })
                .block();
    }
}
