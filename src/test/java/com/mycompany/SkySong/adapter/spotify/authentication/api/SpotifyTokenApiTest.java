package com.mycompany.SkySong.adapter.spotify.authentication.api;

import com.mycompany.SkySong.adapter.exception.common.RequestTimeoutException;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.exception.TokenRequestClientException;
import com.mycompany.SkySong.adapter.spotify.authentication.exception.TokenRequestServerException;
import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpotifyTokenApiTest extends BaseWireMock {

    private SpotifyTokenApi spotifyTokenApi;
    private String tokenResponse;
    private WebClient webClient;


    @BeforeEach
    void setup() throws IOException {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/spotify/auth")
                .build();

        tokenResponse = JsonFileLoader.loadJson("spotify_access_token_response.json");

        spotifyTokenApi = new SpotifyTokenApi("clientId", "clientSecret", webClient);
    }

    @Test
    void whenClientSecretIsNull_ThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpotifyTokenApi("clientId", null, webClient));
    }

    @Test
    void whenClientIdIsNull_ThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpotifyTokenApi(null, "clientSecret", webClient));
    }

    @Test
    void whenTokenRequestSuccessful_ReturnNonEmptyAccessToken() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(tokenResponse)));

        SpotifyTokenResponse response = sendTokenRequest();

        assertThat(response.accessToken())
                .isNotEmpty()
                .isNotNull();
    }

    @Test
    void whenRequestFailsWith4xxError_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"Client error\"")));

        assertThrows(TokenRequestClientException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith5xxError_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"Server error\"")));

        assertThrows(TokenRequestServerException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestTimeout_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withFixedDelay(6000)));

        assertThrows(RequestTimeoutException.class, this::sendTokenRequest);
    }



    private SpotifyTokenResponse sendTokenRequest() {
        SpotifyAccessTokenRequest tokenRequest = new SpotifyAccessTokenRequest(
                "authorization_code", "code", "http://localhost:8080/callback");
        return spotifyTokenApi.sendTokenRequest(tokenRequest.toMultiValueMap());
    }
}
