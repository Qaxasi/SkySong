package com.mycompany.SkySong.adapter.geocoding.api;

import com.mycompany.SkySong.adapter.geocoding.dto.GeocodingResult;
import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class GeocodingApiClientTest extends BaseWireMock {

    private GeocodingApiClient geocodingApi;
    private String geocodingResponse;

    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/geocode")
                .build();

        geocodingResponse = JsonFileLoader.loadJson("geocoding_response.json");

        geocodingApi = new GeocodingApiClient(webClient, "test-api-key");
    }
}
