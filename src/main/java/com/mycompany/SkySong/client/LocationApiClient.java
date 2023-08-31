package com.mycompany.SkySong.client;

import com.mycompany.SkySong.entity.LocationRequest;
import com.mycompany.SkySong.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.Duration;


@Component
@Slf4j
public class LocationApiClient {

    private static final String GEOCODING_API_URL_TEMPLATE =
            "http://api.openweathermap.org/geo/1.0/direct";
    private final String API_KEY = System.getenv("WEATHER_API_KEY");
    private final Duration timeout = Duration.ofSeconds(5);
    private final WebClient webClient;

    public LocationApiClient(WebClient webClient) {
        this.webClient = WebClient.builder()
                .baseUrl(GEOCODING_API_URL_TEMPLATE)
                .build();
    }
    public LocationRequest fetchGeocodingData(String locationName) throws IOException {
        validateLocationName(locationName);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", locationName)
                        .queryParam("appid", API_KEY)
                        .build())
                .retrieve()
                .bodyToFlux(LocationRequest.class)
                .next()
                .timeout(timeout)
                .doOnError(e -> log.error("An error occurred while fetching geocoding data", e)).block();
    }
    private void validateLocationName(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            throw new ValidationException(
                    "Location name cannot be null or empty. First you need to specify your location.");
        }
    }
}

