package com.mycompany.SkySong.music.service;

import com.mycompany.SkySong.music.authorization.service.SpotifyAuthorizationService;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

class SpotifyAuthenticationServiceTest {
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

}
