package com.mycompany.SkySong.client;

import com.mycompany.SkySong.constants.GeocodingConstants;
import com.mycompany.SkySong.entity.WeatherRequest;
import com.mycompany.SkySong.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Component
@Slf4j
public class WeatherApiClient {
    private final String API_KEY;
    private final WebClient webClient;
    @Autowired
    public WeatherApiClient(@Qualifier("weather") WebClient webClient,
                            @Value("${WEATHER_API_KEY}") String API_KEY) {
        this.webClient = webClient;
        this.API_KEY = API_KEY;
    }

    public WeatherRequest fetchWeatherData(double latitude, double longitude) throws IOException {
        if (latitude < GeocodingConstants.MINIMUM_VALUES_FOR_LATITUDE ||
                latitude > GeocodingConstants.MAXIMUM_VALUES_FOR_LATITUDE ||
                longitude < GeocodingConstants.MINIMUM_VALUES_FOR_LONGITUDE
                || longitude > GeocodingConstants.MAXIMUM_VALUES_FOR_LONGITUDE) {
            throw new ValidationException("Invalid latitude or longitude values.");
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("appid", API_KEY)
                        .build())
                .retrieve()
                .bodyToFlux(WeatherRequest.class)
                .next()
                .doOnError(e -> log.error("An error occurred while fetching weather data", e))
                .block();


    }
}
