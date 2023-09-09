package com.mycompany.SkySong.location.client;

import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
                   .retrieve()
                   .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                       log.error("4xx error while fetching geocoding data, status code: {}", clientResponse.statusCode());
                       return Mono.error(new IllegalArgumentException("Invalid request parameters"));
                   })
                   .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                       log.error("5xx error while fetching geocoding data, status code: {}", clientResponse.statusCode());
                       return Mono.error(new IllegalArgumentException("Server error occurred"));
                   })
                   .bodyToFlux(LocationRequest.class)
                   .next()
                   .timeout(timeout)
                   .doOnError(e -> log.error("An error occurred while fetching geocoding data", e)).block();
       } catch (ValidationException ex) {
           log.error("Validation Exception: {}", ex.getMessage());
           throw ex;
       } catch (Exception e) {
           log.error("General Exception: {}", e.getMessage());
           throw new IOException("Failed to fetch geocoding data",e);
       }
    }
    private void validateLocationName(String locationName) {
        Optional.ofNullable(locationName)
                .filter(location -> !location.trim().isEmpty())
                .orElseThrow(() -> new ValidationException(
                        "Location name cannot be null. First you need to specify your location."));
    }
}

