package com.mycompany.SkySong.adapter.spotify.authentication.api;

import com.mycompany.SkySong.adapter.exception.common.AuthorizationException;
import com.mycompany.SkySong.adapter.exception.common.RequestTimeoutException;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.exception.TokenRequestClientException;
import com.mycompany.SkySong.adapter.spotify.authentication.exception.TokenRequestServerException;
import com.mycompany.SkySong.adapter.spotify.authentication.xyz.SpotifyTokenValidator;
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
    private final SpotifyTokenValidator tokenValidator;

    public SpotifyTokenApi(@Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                           @Value("${SPOTIFY_CLIENT_SECRET}") String spotifyClientSecret,
                           @Qualifier("spotifyWebClient") WebClient webClient,
                           SpotifyTokenValidator tokenValidator) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.webClient = webClient;
        this.tokenValidator = tokenValidator;
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
                            throw new AuthorizationException("Unauthorized access: Invalid client credentials");
                        } else if (response.statusCode() == HttpStatus.FORBIDDEN) {
                            log.error("Forbidden: Access denied by Spotify");
                            throw new AuthorizationException("Access denied: The application does not have permission to access this resource");
                        } else {
                            log.error("Client error while retrieving token. Status - {}", response.statusCode());
                            throw new TokenRequestClientException("Client error: " + response.statusCode());
                        }
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        log.error("Server error while retrieving token. Status: {}", response.statusCode());
                        throw new TokenRequestServerException("Server error: " + response.statusCode());
                    })
                    .bodyToMono(SpotifyTokenResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof TimeoutException) {
                throw new RequestTimeoutException("Request timed out while retrieving token", ex);
            }
            throw ex;
        }
    }

    private Result<SpotifyTokenResponse> sendTokenRequestWithValidation(MultiValueMap<String, String> bodyData,
                                                                       Function<SpotifyTokenResponse, Result<Void>> validator) {
        SpotifyTokenResponse response = sendTokenRequest(bodyData);

        Result<Void> validationResult = validator.apply(response);
        if (!validationResult.success()) {
            return Result.failure(validationResult.errorMessage());
        }
        return Result.success(response);
    }

    public Result<SpotifyTokenResponse> sendAccessTokenRequest(SpotifyAccessTokenRequest request) {
        return sendTokenRequestWithValidation(request.toMultiValueMap(), tokenValidator::validateAccessTokenResponse);
    }

    public Result<SpotifyTokenResponse> sendRefreshTokenRequest(SpotifyRefreshTokenRequest request) {
        return sendTokenRequestWithValidation(request.toMultiValueMap(), tokenValidator::validateRefreshTokenResponse);
    }
}