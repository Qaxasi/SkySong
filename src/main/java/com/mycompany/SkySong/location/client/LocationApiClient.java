package com.mycompany.SkySong.location.client;

import com.mycompany.SkySong.exception.AuthorizationException;
import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.LocationNotGiven;
import com.mycompany.SkySong.location.exception.TooManyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;


@Component
@Slf4j
public class LocationApiClient {
    private final String API_KEY;
    private final Duration timeout = Duration.ofSeconds(5);
    private final WebClient webClient;
    @Autowired
    public LocationApiClient(@Qualifier("location") WebClient webClient,
                             @Value("${WEATHER_API_KEY}") String API_KEY) {
        this.webClient = webClient;
        this.API_KEY = API_KEY;
    }

    public LocationRequest fetchGeocodingData(String locationName) throws IOException {
        try {
            validateLocationName(locationName);
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", locationName)
                            .queryParam("appid", API_KEY)
                            .build())
                    .exchangeToFlux(clientResponse -> {
                        HttpStatusCode status = clientResponse.statusCode();
                        if (status.equals(HttpStatus.TOO_MANY_REQUESTS)) {
                            log.error("Exceeded number of allowed calls to Geocoding API. Please try again later: {}",
                                    clientResponse.statusCode());
                            return Flux.error(new TooManyRequestException(
                                    "Exceeded number of allowed calls to Geocoding API. Please try again later."));
                        } else if (status.equals(HttpStatus.UNAUTHORIZED)) {
                            log.error("Invalid authorization token");
                            return Flux.error(new AuthorizationException("Invalid authorization token"));
                        }
                        return clientResponse.bodyToFlux(LocationRequest.class);
                    })
                    .next()
                    .timeout(timeout)
                    .doOnError(e -> log.error("An error occurred while fetching geocoding data", e))
                    .block();
        } catch (LocationNotGiven e) {
            log.error("LocationNotGiven Exception: {}", e.getMessage());
            throw e;
        }
    }
    private void validateLocationName(String locationName) {
        Optional.ofNullable(locationName)
                .filter(location -> !location.trim().isEmpty())
                .orElseThrow(() -> new LocationNotGiven(
                        "Location name cannot be null or empty. First you need to specify your location."));
    }
}

