package com.mycompany.SkySong.adapter.weather.api;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.weather.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class WeatherApiClient {
    private final String apiKey;
    private final WebClient webClient;
    private final Duration timeout;

    public WeatherApiClient(@Value("${WEATHER_API_KEY}") String apiKey,
                            @Qualifier("weatherWebClient") WebClient webClient) {
        this.apiKey = Objects.requireNonNull(apiKey, "API_KEY cannot be null");
        this.webClient = Objects.requireNonNull(webClient, "WebClient cannot be null");
        this.timeout = Duration.ofSeconds(5);
    }

    public WeatherResponse fetchWeatherData(double lat, double lon) {
        try {
            WeatherResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                        log.error("Exceeded number of allowed calls to Weather API: {}", res.statusCode());
                        return Mono.error(new TooManyRequestsException(
                                "Exceeded number of allowed calls to Weather API. Please try again later."));
                    })
                    .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                        log.error("Invalid authorization token: {}", res.statusCode());
                        return Mono.error(new AuthorizationException("Invalid authorization token."));
                    })
                    .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                        log.error("Server is unavailable: {}", res.statusCode());
                        return Mono.error(new ServiceUnavailableException(
                                "Failed to fetch weather data. Please try again later."));
                    })
                    .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, res -> {
                        log.error("An error occurred while fetching weather data: {}", res.statusCode());
                        return Mono.error(new InternalServerErrorException(
                                "An error occurred while fetching weather data."));
                    })
                    .bodyToMono(WeatherResponse.class)
                    .timeout(timeout)
                    .block();

            validateWeatherResponse(response);
            return response;

        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof TimeoutException) {
                throw new RequestTimeoutException("Request timed out while fetching geocoding data", ex);
            }
            throw ex;
        }
    }

    private void validateWeatherResponse(WeatherResponse response) {
        if (response == null) {
            log.error("Received null response from Weather API ");
            throw new NullPointerException("Weather API response is null");
        }

        if (response.daytimeInfo() == null || response.atmosphericConditions() == null ||
                response.windInfo() == null || response.cloudsInfo() == null || response.weather().isEmpty()) {
            log.error("Incomplete weather data received: {}", response);
            throw new DataNotFoundException("Received incomplete weather data.");
        }
    }
}
