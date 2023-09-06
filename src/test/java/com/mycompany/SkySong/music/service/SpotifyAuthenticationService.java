package com.mycompany.SkySong.music.service;

import com.mycompany.SkySong.music.authorization.service.SpotifyAuthorizationService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class SpotifyAuthenticationService {
    private SpotifyAuthorizationService service;
    private static MockWebServer mockWebServer;
    private final String clientSecret = "testClientSecret";

    @BeforeAll
    static void beforeAll() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void setup() {
        final var webClient = WebClient.builder()
                .baseUrl(String.format("http://localhost:%s/token", mockWebServer.getPort()))
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .build();

        service = new SpotifyAuthorizationService(
                "clientId", clientSecret, webClient, "redirect_uri");
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.close();
    }
    @Test
    void getAccessTokenShouldSendRequestWithEncodedCredentialsInHeader() throws InterruptedException {
        final var expectedToken =
                "{" +
                        "\"access_token\": \"TEST_TOKEN\"," +
                        "\"token_type\": \"Bearer\"," +
                        "\"expires_in\": 3600" +
                        "}";

        final var encodedCredentials = Base64.getEncoder()
                .encodeToString(("clientId:testClientSecret").getBytes());
        final var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(expectedToken)
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        service.getAccessToken("test-auth-code");

        final var recordRequest = mockWebServer.takeRequest();
        assertEquals("Basic " + encodedCredentials, recordRequest.getHeaders().get("Authorization"));

    }

    @Test
    void getAccessTokenShouldSendPostRequest() throws InterruptedException {
        final var expectedToken =
                "{" +
                        "\"access_token\": \"TEST_TOKEN\"," +
                        "\"token_type\": \"Bearer\"," +
                        "\"expires_in\": 3600" +
                        "}";
        final var mockResponse = new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(expectedToken)
                .setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        service.getAccessToken("test-auth-code");

        final var recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
    }

    @Test
    void authorizationUrlShouldContainAllNecessaryParams() {
        final var authorizationUrl = service.getAuthorizationCodeURL();
        assertTrue(authorizationUrl.contains("client_id" ));
        assertTrue(authorizationUrl.contains("response_type"));
        assertTrue(authorizationUrl.contains("redirect_uri"));
        assertTrue(authorizationUrl.contains("scope"));
    }

    @Test
    void shouldReturnAuthorizationCodeURL() {
        final var authorizationURL = service.getAuthorizationCodeURL();
        assertNotNull(authorizationURL);
        assertTrue(authorizationURL.startsWith("https://accounts.spotify.com/authorize"));

    }






}
