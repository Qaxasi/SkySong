package com.mycompany.SkySong.location.client;

import com.mycompany.SkySong.exception.AuthorizationException;
import com.mycompany.SkySong.exception.ServerIsUnavailable;
import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.LocationNotGiven;
import com.mycompany.SkySong.location.exception.TooManyRequestsException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
                .baseUrl(String.format("http://localhost:%s/api/location/coordinates/",
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
    void fetchGeocodingDataShouldSendGetRequest() throws InterruptedException {
        final var expectedBody =
                "{" +
                        "\"name\": \"Test-Location\"," +
                        "\"lat\": 50.12345," +
                        "\"lon\": 20.12345," +
                        "\"country\": \"Test-Country\"," +
                        "\"state\": \"Test-State\"" +
                        "}";

        final var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(expectedBody)
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);
        locationApiClient.fetchGeocodingData("Test-Location");

        final var recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void shouldCorrectlyMapApiResponseToLocationRequest() {
        final var expectedBody =
                "{" +
                        "\"name\": \"Test-Location\"," +
                        "\"lat\": 50.12345," +
                        "\"lon\": 20.12345," +
                        "\"country\": \"Test-Country\"," +
                        "\"state\": \"Test-State\"" +
                        "}";

        final var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(expectedBody)
                .setResponseCode(200);

        mockWebServer.enqueue(mockResponse);

        LocationRequest locationRequest = locationApiClient.fetchGeocodingData("Test-Location");

        assertEquals("Test-Location", locationRequest.locationName());
        assertEquals(50.12345, locationRequest.latitude());
        assertEquals(20.12345, locationRequest.longitude());
        assertEquals("Test-Country", locationRequest.country());
        assertEquals("Test-State", locationRequest.state());
    }
    @Test
    void shouldSendRequestToCorrectURI() throws InterruptedException{
        final var mockResponse = new MockResponse()
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);
        locationApiClient.fetchGeocodingData("Test-Location");

        final var recordedRequest = mockWebServer.takeRequest();
        assertTrue(recordedRequest.getPath().contains("q=Test-Location"));
        assertTrue(recordedRequest.getPath().contains("appid=" + clientSecret));
    }
    @Test
    void shouldThrowExceptionForSlowResponses() {
        final var mockResponse = new MockResponse()
                .setBody("{}")
                .setResponseCode(200)
                .setBodyDelay(6, TimeUnit.SECONDS);
        mockWebServer.enqueue(mockResponse);
        Exception exception = assertThrows(Exception.class,
                () -> locationApiClient.fetchGeocodingData("Test-Location"));
        if (!(exception instanceof TimeoutException)) {
            Throwable cause = exception.getCause();
            assertTrue(cause instanceof TimeoutException);
        }
    }


    @Test
    void shouldThrowAuthorizationExceptionInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(401));

        assertThrows(AuthorizationException.class, () -> locationApiClient.fetchGeocodingData(
                "Test-Location"));
    }

    @Test
    void shouldThrowToManyRequestErrorInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(429));

        assertThrows(TooManyRequestsException.class, () ->
                locationApiClient.fetchGeocodingData("Test-Location"));
    }
    @Test
    void shouldThrowServerIsUnavailableExceptionInTheMethodFetchGeocodingData() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(503));

        assertThrows(ServerIsUnavailable.class, () ->
                locationApiClient.fetchGeocodingData("Test-Location"));
    }

}
