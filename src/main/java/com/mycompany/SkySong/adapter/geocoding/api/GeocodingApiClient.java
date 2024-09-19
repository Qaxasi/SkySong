package com.mycompany.SkySong.adapter.geocoding.api;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.geocoding.dto.GeocodingResponse;
import com.mycompany.SkySong.adapter.geocoding.dto.GeocodingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class GeocodingApiClient {

    private final String API_KEY;
    private final WebClient webClient;
    private final Duration timeout;

    public GeocodingApiClient(@Qualifier("geocodingWebClient") WebClient webClient,
                              @Value("${GEOCODING_API_KEY}") String apiKey) {
        this.webClient = Objects.requireNonNull(webClient, "WebClient cannot be null");
        this.API_KEY = Objects.requireNonNull(apiKey, "API_KEY cannot be null");
        this.timeout = Duration.ofSeconds(5);
    }

    public GeocodingResult fetchGeocodingData(String locationName) {
        try {
            GeocodingResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("text", locationName)
                            .queryParam("format", "json")
                            .queryParam("apiKey", API_KEY)
                            .queryParam("limit", 1)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                        log.error("Exceeded number of allowed calls to Geocoding API: {}", res.statusCode());
                        return Mono.error(new TooManyRequestsException(
                                "Exceeded number of allowed calls to Geocoding API. Please try again later."));
                    })
                    .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                        log.error("Invalid authorization token: {}", res.statusCode());
                        return Mono.error(new AuthorizationException("Invalid authorization token."));
                    })
                    .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                        log.error("Server is unavailable: {}", res.statusCode());
                        return Mono.error(new ServiceUnavailableException(
                                "Failed to fetch geocoding data. Please try again later."));
                    })
                    .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, res -> {
                        log.error("An error occurred while fetching geocoding data: {}", res.statusCode());
                        return Mono.error(new InternalServerErrorException(
                                "An error occurred while fetching geocoding data."));
                    })
                    .bodyToMono(GeocodingResponse.class)
                    .timeout(timeout)
                    .block();

            return extractResult(response);
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof TimeoutException) {
                throw new WebClientTimeoutException("Request timed out while fetching geocoding data", ex);
            }
            throw ex;
        }
    }

    private GeocodingResult extractResult(GeocodingResponse response) {
        if (response == null || response.results() == null || response.results().isEmpty()) {
            throw new DataNotFoundException("The specified location could not be found in our data source.");
        }
        return response.results().get(0);
    }
}