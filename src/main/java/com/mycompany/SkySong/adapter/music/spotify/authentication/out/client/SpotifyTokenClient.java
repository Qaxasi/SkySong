package com.mycompany.SkySong.adapter.music.spotify.authentication.out.client;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.music.spotify.authentication.out.dto.SpotifyTokenResponse;
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
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                        log.error("Unauthorized: Invalid Spotify credentials");
                        return Mono.error(new ApiAuthenticationException(
                                "Unable to authenticate with Spotify. Please check your credentials."));
                    } else if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                        log.warn("Too many requests sent to Spotify API");
                        return Mono.error(new ApiTooManyRequestsException(
                                "You've made too many requests in a short period. Please wait a moment and try again."));
                    } else if (response.statusCode() == HttpStatus.BAD_REQUEST) {
                        log.warn("Bad request: Request to Spotify is malformed or contains invalid parameters");
                        return Mono.error(new ApiBadRequestException(
                                "There seems to be an issue with the request to Spotify. Please check your input and try again."));
                    }
                    return Mono.empty();
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("Server error while retrieving token from Spotify. Status - {}", response.statusCode());
                    return Mono.error(new ApiServerErrorException(
                            "Spotify is currently experiencing technical issues. Please try again later."));
                })
                .bodyToMono(SpotifyTokenResponse.class)
                .onErrorMap(TimeoutException.class, ex ->
                        new ApiRequestTimeoutException("The request to Spotify timed out. Please check your connection and try again."))
                .block();
    }
}
