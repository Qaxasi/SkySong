package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.api;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.validation.SpotifyRefreshTokenValidator;
import com.mycompany.SkySong.shared.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class SpotifyRefreshTokenApi {
    private final String spotifyClientId;
    private final String spotifyClientSecret;
    private final WebClient webClient;
    private final SpotifyRefreshTokenValidator validator;

    public SpotifyRefreshTokenApi(@Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                                  @Value("${SPOTIFY_CLIENT_SECRET}") String spotifyClientSecret,
                                  @Qualifier("spotifyWebClient") WebClient webClient,
                                  SpotifyRefreshTokenValidator validator) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.webClient = webClient;
        this.validator = validator;
    }

    public Result<SpotifyTokenResponse> fetchRefreshToken(SpotifyRefreshTokenRequest request) {
        SpotifyTokenResponse response = sendTokenRequest(request.toMultiValueMap());
        Result<Void> validationResult = validator.validateResponse(response);
        if (!validationResult.success()) {
            return Result.failure(validationResult.errorMessage(), validationResult.errorType());
        }
        return Result.success(response);
    }

    private SpotifyTokenResponse sendTokenRequest(MultiValueMap<String, String> bodyData) {
        try {
            return webClient.post()
                    .uri("/api/token")
                    .headers(headers -> {
                        headers.setBasicAuth(spotifyClientId, spotifyClientSecret);
                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    })
                    .bodyValue(bodyData)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                            log.error("Unauthorized: Invalid Spotify credentials");
                            throw new ApiAuthenticationException("Unable to authenticate with Spotify. Please check your credentials.");
                        } else if (response.statusCode() == HttpStatus.FORBIDDEN) {
                            log.error("Forbidden: Access denied by Spotify");
                            throw new ApiAccessDeniedException("Access to Spotify has been denied. Please ensure the necessary permissions are granted.");
                        } else {
                            log.error("Client error while retrieving token. Status - {}", response.statusCode());
                            throw new ApiClientErrorException("There was an issue with your request to Spotify. Please try again later.");
                        }
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        log.error("Server error while retrieving token. Status: {}", response.statusCode());
                        throw new ApiServerErrorException("Spotify encountered an error while processing the request. Please try again later.");
                    })
                    .bodyToMono(SpotifyTokenResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof TimeoutException) {
                throw new ApiRequestTimeoutException("The request to Spotify timed out. Please check your connection and try again.", ex);
            }
            throw ex;
        }
    }
}
