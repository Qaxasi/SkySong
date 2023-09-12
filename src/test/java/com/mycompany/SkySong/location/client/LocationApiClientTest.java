package com.mycompany.SkySong.location.client;

import com.mycompany.SkySong.exception.AuthorizationException;
import com.mycompany.SkySong.exception.ServerIsUnavailable;
import com.mycompany.SkySong.exception.ValidationException;
import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.LocationNotGiven;
import com.mycompany.SkySong.location.exception.TooManyRequestException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
    void fetchGeocodingDataShouldSendGetRequest() throws IOException, InterruptedException {
        final var expectedBody =
                "{" +
                        "\"locationName\": \"Kielce\"," +
                        "\"latitude\": 50.85403585," +
                        "\"longitude\": 20.609914352101452," +
                        "\"country\": \"PL\"," +
                        "\"state\": \"Świętokrzyskie Voivodeship\"" +
                        "}";

        final var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(expectedBody)
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);
        locationApiClient.fetchGeocodingData("Kielce");

        final var recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void shouldFetchGeocodingDataForLocation() throws IOException {
        final var expectedBody =
                "{" +
                        "\"name\": \"Kielce\"," +
                        "\"lat\": 50.85403585," +
                        "\"lon\": 20.609914352101452," +
                        "\"country\": \"PL\"," +
                        "\"state\": \"Świętokrzyskie Voivodeship\"" +
                        "}";

        final var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(expectedBody)
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        LocationRequest locationRequest = locationApiClient.fetchGeocodingData("Kielce");

        assertEquals("Kielce", locationRequest.locationName());
        assertEquals(50.85403585, locationRequest.latitude());
        assertEquals(20.609914352101452, locationRequest.longitude());
        assertEquals("PL", locationRequest.country());
        assertEquals("Świętokrzyskie Voivodeship", locationRequest.state());
    }

    @Test
    void shouldThrowLocationNotGivenExceptionWhenFetchingGeocodingDataWithInvalidName() {
        assertThrows(LocationNotGiven.class, () -> locationApiClient.fetchGeocodingData(null));

        assertThrows(LocationNotGiven.class, () -> locationApiClient.fetchGeocodingData(""));
    }

    @Test
    void shouldThrowAuthorizationExceptionInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(401));

        assertThrows(AuthorizationException.class, () -> locationApiClient.fetchGeocodingData(
                "Location"));
    }

    @Test
    void shouldThrowToManyRequestErrorInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(429));

        assertThrows(TooManyRequestException.class, () -> locationApiClient.fetchGeocodingData("Location"));
    }
    @Test
    void shouldThrowServerIsUnavailableExceptionInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(503));

        assertThrows(ServerIsUnavailable.class, () -> locationApiClient.fetchGeocodingData("Kielce"));
    }

}
