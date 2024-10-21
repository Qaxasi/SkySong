package com.mycompany.SkySong.adapter.spotify.authentication.api;

import com.mycompany.SkySong.adapter.exception.common.AuthorizationException;
import com.mycompany.SkySong.adapter.exception.common.RequestTimeoutException;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.exception.TokenRequestClientException;
import com.mycompany.SkySong.adapter.spotify.authentication.exception.TokenRequestServerException;
import com.mycompany.SkySong.adapter.spotify.authentication.validation.SpotifyAccessTokenValidator;
import com.mycompany.SkySong.adapter.spotify.authentication.validation.SpotifyRefreshTokenValidator;
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
import java.util.function.Function;

@Service
@Slf4j
public class SpotifyTokenApi {
    private final String spotifyClientId;
    private final String spotifyClientSecret;
    private final WebClient webClient;
    private final SpotifyAccessTokenValidator accessTokenValidator;
    private final SpotifyRefreshTokenValidator refreshTokenValidator;

    public SpotifyTokenApi(@Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                           @Value("${SPOTIFY_CLIENT_SECRET}") String spotifyClientSecret,
                           @Qualifier("spotifyWebClient") WebClient webClient,
                           SpotifyAccessTokenValidator accessTokenValidator,
                           SpotifyRefreshTokenValidator refreshTokenValidator) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.webClient = webClient;
        this.accessTokenValidator = accessTokenValidator;
        this.refreshTokenValidator = refreshTokenValidator;
    }

    public Result<SpotifyTokenResponse> sendAccessTokenRequest(SpotifyAccessTokenRequest request) {
        return sendTokenRequestWithValidation(request.toMultiValueMap(), accessTokenValidator::validateResponse);
    }

    public Result<SpotifyTokenResponse> sendRefreshTokenRequest(SpotifyRefreshTokenRequest request) {
        return sendTokenRequestWithValidation(request.toMultiValueMap(), refreshTokenValidator::validateResponse);
    }

    private Result<SpotifyTokenResponse> sendTokenRequestWithValidation(MultiValueMap<String, String> bodyData,
                                                                        Function<SpotifyTokenResponse, Result<Void>> validator) {
        SpotifyTokenResponse response = sendTokenRequest(bodyData);

        Result<Void> validationResult = validator.apply(response);
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
                            throw new AuthorizationException("Unable to authenticate with Spotify. Please check your credentials.");
                        } else if (response.statusCode() == HttpStatus.FORBIDDEN) {
                            log.error("Forbidden: Access denied by Spotify");
                            throw new AuthorizationException("Access to Spotify has been denied. Please ensure the necessary permissions are granted.");
                        } else {
                            log.error("Client error while retrieving token. Status - {}", response.statusCode());
                            throw new TokenRequestClientException("There was an issue with your request to Spotify. Please try again late");
                        }
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        log.error("Server error while retrieving token. Status: {}", response.statusCode());
                        throw new TokenRequestServerException("Spotify is currently unavailable. Please try again later");
                    })
                    .bodyToMono(SpotifyTokenResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof TimeoutException) {
                throw new RequestTimeoutException("The request to Spotify timed out. Please check your connection and try again.", ex);
            }
            throw ex;
        }
    }
}