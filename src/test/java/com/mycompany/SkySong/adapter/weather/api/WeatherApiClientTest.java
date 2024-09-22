package com.mycompany.SkySong.adapter.weather.api;

import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

public class WeatherApiClientTest extends BaseWireMock {

    private WeatherApiClient weatherApiClient;
    private String weatherResponse;

    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/weather")
                .build();

        weatherResponse = JsonFileLoader.loadJson("weather_response.json");

        weatherApiClient = new WeatherApiClient("test-api-key", webClient);
    }
}
