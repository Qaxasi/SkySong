package com.mycompany.SkySong.location.client;

import com.mycompany.SkySong.exception.AuthorizationException;
import com.mycompany.SkySong.exception.ServerIsUnavailable;
import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.TooManyRequestsException;
import com.mycompany.SkySong.location.exception.WebClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Objects;


@Component
@Slf4j
public class LocationApiClient {
    private final String API_KEY;
    private final Duration timeout;
    private final WebClient webClient;
    @Autowired
    public LocationApiClient(@Qualifier("location") WebClient webClient,
                             @Value("${WEATHER_API_KEY}") String API_KEY) {
        this.webClient = Objects.requireNonNull(webClient, "WebClient cannot be null");
        this.API_KEY = Objects.requireNonNull(API_KEY, "API_KEY cannot be null");
        this.timeout = Duration.ofSeconds(5);
    }

    public LocationRequest fetchGeocodingData(String locationName) {
        return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", locationName)
                            .queryParam("appid", API_KEY)
                            .build())
                    .exchangeToFlux(this::handleClientResponse)
                    .next()
                    .timeout(timeout)
                    .block();
        }

        private Flux<LocationRequest> handleClientResponse(ClientResponse clientResponse) {
            HttpStatusCode status = clientResponse.statusCode();
            if (status.equals(HttpStatus.TOO_MANY_REQUESTS)) {
                log.error("Exceeded number of allowed calls to Geocoding API: {}", clientResponse.statusCode());
                return Flux.error(new TooManyRequestsException(
                        "Exceeded number of allowed calls to Geocoding API. Please try again later."));
            } else if (status.equals(HttpStatus.UNAUTHORIZED)) {
                log.error("Invalid authorization token: {}", clientResponse.statusCode());
                return Flux.error(new AuthorizationException("Invalid authorization token."));
            } else if (status.equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                log.error("Server is unavailable: {}", clientResponse.statusCode());
                return Flux.error(new ServerIsUnavailable(
                        "Failed to fetch geocoding data. Please try again later."));
            } else if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                log.error("An error occurred while fetching geocoding data: {}", clientResponse.statusCode());
                return Flux.error(new WebClientException(
                        "An error occurred while fetching geocoding data."));
            }
            return clientResponse.bodyToFlux(LocationRequest.class);
        }
}

