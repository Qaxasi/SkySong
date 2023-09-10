package com.mycompany.SkySong.location.client;

import com.mycompany.SkySong.exception.ValidationException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertThrows;


class LocationApiClientTest {

    private LocationApiClient locationApiClient;
    private static MockWebServer mockWebServer;
    private final String clientSecret = "testClientSecret";
    private WebClient webClient;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }
    @BeforeEach
    void setup() {
        webClient = WebClient.builder()
                .baseUrl(String.format("http://localhost:%s/api/location/coordinates/locationName",
                        mockWebServer.getPort()))
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .build();

        locationApiClient = new LocationApiClient(webClient, clientSecret);
    }
    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.close();
    }


    @Test
    void shouldThrowValidationExceptionWhenFetchingGeocodingDataWithInvalidName() {
        assertThrows(ValidationException.class, () -> locationApiClient.fetchGeocodingData(null));

        assertThrows(ValidationException.class, () -> locationApiClient.fetchGeocodingData(" "));
    }
    @Test
    void shouldThrowServerErrorInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(IOException.class, () -> locationApiClient.fetchGeocodingData(
                "Server Error"));
    }

    @Test
    void shouldThrowClientErrorInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        assertThrows(IOException.class, () -> locationApiClient.fetchGeocodingData(
                "Client Error"));
    }





}
