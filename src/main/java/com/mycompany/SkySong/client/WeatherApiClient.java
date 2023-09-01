package com.mycompany.SkySong.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.constants.GeocodingConstants;
import com.mycompany.SkySong.entity.WeatherRequest;
import com.mycompany.SkySong.exception.ValidationException;
import com.mycompany.SkySong.exception.WeatherException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Component
@Slf4j
public class WeatherApiClient {
    private static final String WEATHER_API_URL_TEMPLATE =
            "https://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = System.getenv("WEATHER_API_KEY");
    private final WebClient webClient;
    public WeatherApiClient(WebClient webClient) {
        this.webClient = WebClient.builder()
                .baseUrl(WEATHER_API_URL_TEMPLATE)
                .build();
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
