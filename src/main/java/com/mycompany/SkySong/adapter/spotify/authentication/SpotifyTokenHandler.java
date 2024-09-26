package com.mycompany.SkySong.adapter.spotify.authentication;

import com.mycompany.SkySong.adapter.exception.common.RequestTimeoutException;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.exception.TokenRequestClientException;
import com.mycompany.SkySong.adapter.spotify.exception.TokenRequestServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

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

    public SpotifyAccessTokenResponse getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> formData = new SpotifyAccessTokenRequest("authorization_code", authorizationCode, redirectUri).toMultiValueMap();
        return sendTokenRequest(formData);
    }

    public SpotifyAccessTokenResponse refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> formData = new SpotifyRefreshTokenRequest("refresh_token", refreshToken).toMultiValueMap();
        return sendTokenRequest(formData);
    }

    public SpotifyAccessTokenResponse sendTokenRequest(MultiValueMap<String, String> bodyData) {
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
                        String errorBody = response.bodyToMono(String.class).block();
                        log.error("Client error while retrieving token. Status: {}, Body: {}", response.statusCode(), errorBody);
                        throw new TokenRequestClientException("Client error: " + errorBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        String errorBody = response.bodyToMono(String.class).block();
                        log.error("Server error while retrieving token. Status: {}, Body: {}", response.statusCode(), errorBody);
                        throw new TokenRequestServerException("Server error: " + errorBody);
                    })
                    .bodyToMono(SpotifyAccessTokenResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof TimeoutException) {
                throw new RequestTimeoutException("Request timed out while retrieving token", ex);
            }
            throw ex;
        }
    }
}