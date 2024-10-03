package com.mycompany.SkySong.adapter.spotify.api;

import com.mycompany.SkySong.adapter.exception.common.RequestTimeoutException;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyTokenResponse;
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
public class SpotifyTokenClient {
    private final String spotifyClientId;
    private final String spotifyClientSecret;
    private final WebClient webClient;

    public SpotifyTokenClient(@Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                              @Value("${SPOTIFY_CLIENT_SECRET}") String spotifyClientSecret,
                              @Qualifier("spotifyWebClient") WebClient webClient) {
        if (spotifyClientId == null || spotifyClientId.isEmpty()) {
            throw new IllegalArgumentException("Spotify client id cannot be null or empty");
        }

        if (spotifyClientSecret == null || spotifyClientSecret.isEmpty()) {
            throw new IllegalArgumentException("Spotify client secret cannot be null or empty");
        }

        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.webClient = webClient;
    }

    public SpotifyTokenResponse sendTokenRequest(MultiValueMap<String, String> bodyData) {
        if (bodyData == null || bodyData.isEmpty()) {
            throw new IllegalArgumentException("Request body cannot be null or empty");
        }

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
}