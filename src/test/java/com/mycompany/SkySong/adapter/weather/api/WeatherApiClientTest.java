package com.mycompany.SkySong.adapter.weather.api;

import com.mycompany.SkySong.adapter.exception.common.AuthorizationException;
import com.mycompany.SkySong.adapter.weather.dto.WeatherResponse;
import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void whenFetchingWeatherData_ReturnCorrectWeatherInfo() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/weather"))
                .withQueryParam("lat", equalTo("52.2299"))
                .withQueryParam("lon", equalTo("21.0065"))
                .withQueryParam("appid", equalTo("test-api-key"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(weatherResponse)));

        WeatherResponse response = fetchWeatherData(52.2299, 21.0065);

        assertAll(
                () -> assertThat(response.weather().get(0).condition()).isEqualTo("Clear"),
                () -> assertThat(response.weather().get(0).condition()).isEqualTo("Clear"),
                () -> assertThat(response.atmosphericConditions().temperature()).isEqualTo(289.76),
                () -> assertThat(response.atmosphericConditions().humidity()).isEqualTo(69),
                () ->assertThat(response.cloudsInfo().cloudCoverage()).isEqualTo(0),
                () -> assertThat(response.windInfo().speed()).isEqualTo(3.13),
                () -> assertThat(response.daytimeInfo().sunrise()).isEqualTo(1726978940),
                () -> assertThat(response.daytimeInfo().sunset()).isEqualTo(1727022897)
        );
    }

    @Test
    void whenInvalidApiKeyProvided_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/weather"))
                .withQueryParam("lat", equalTo("52.2299"))
                .withQueryParam("lon", equalTo("21.0065"))
                .withQueryParam("appid", equalTo("test-api-key"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"Invalid API key\"")));

        assertThrows(AuthorizationException.class, () -> fetchWeatherData(52.2299, 21.0065));
    }

    private WeatherResponse fetchWeatherData(double lat, double lon) {
        return weatherApiClient.fetchWeatherData(lat, lon);
    }
}
