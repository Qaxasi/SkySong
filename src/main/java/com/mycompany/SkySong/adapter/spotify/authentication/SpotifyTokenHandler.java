package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.exception.common.AuthorizationException;
import com.mycompany.SkySong.adapter.exception.common.RequestTimeoutException;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.exception.TokenRequestClientException;
import com.mycompany.SkySong.adapter.spotify.exception.TokenRequestServerException;
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

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class SpotifyTokenHandler {
    private final String spotifyClientId;
    private final String spotifyClientSecret;
    private final String redirectUri;
    private final WebClient webClient;

    public SpotifyTokenHandler(@Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                               @Value("${SPOTIFY_CLIENT_SECRET}") String spotifyClientSecret,
                               @Value("${REDIRECT_URI}") String redirectUri,
                               @Qualifier("spotifyWebClient") WebClient webClient) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.redirectUri = redirectUri;
        this.webClient = webClient;
    }

    public Mono<SpotifyAccessTokenResponse> getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> formData = new SpotifyAccessTokenRequest("authorization_code", authorizationCode, redirectUri).toMultiValueMap();
        return sendTokenRequest(formData);
    }

    public Mono<SpotifyAccessTokenResponse> refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> formData = new SpotifyRefreshTokenRequest("refresh_token", refreshToken).toMultiValueMap();
        return sendTokenRequest(formData);
    }

    private Mono<SpotifyAccessTokenResponse> sendTokenRequest(MultiValueMap<String, String> bodyData) {
        return webClient.post()
                .uri("/api/token")
                .headers(headers -> {
                    headers.setBasicAuth(spotifyClientId, spotifyClientSecret);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .bodyValue(bodyData)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class).flatMap(errorBody -> {
                    log.error("Client error while retrieving token. Status: {}, Body: {}", response.statusCode(), errorBody);
                    if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                        return Mono.error(new AuthorizationException("Authorization exception: " + errorBody));
                    } else {
                        return Mono.error(new TokenRequestClientException("Client error: " + errorBody));
                    }
                }))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class).flatMap(errorBody -> {
                    log.error("Server error while retrieving token. Status: {}, Body: {}", response.statusCode(), errorBody);
                    return Mono.error(new TokenRequestServerException("Server error: " + errorBody));
                }))
                .bodyToMono(SpotifyAccessTokenResponse.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(TimeoutException.class, e -> {
                    log.error("Timeout while retrieving token", e);
                    throw new RequestTimeoutException("Request timed out while retrieving token", e);
                });
    }
}
