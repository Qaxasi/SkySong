package com.mycompany.SkySong.adapter.spotify.authentication.token.access.api;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.token.access.validation.SpotifyAccessTokenValidator;
import com.mycompany.SkySong.shared.utils.Result;
import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class SpotifyAccessTokenApiTest extends BaseWireMock {
    private SpotifyAccessTokenApi tokenApi;
    private String tokenResponse;

    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/spotify/auth")
                .build();

        tokenResponse = JsonFileLoader.loadJson("spotify_access_token_response.json");


        SpotifyAccessTokenValidator validator = new SpotifyAccessTokenValidator();
        tokenApi = new SpotifyAccessTokenApi("clientId", "clientSecret", webClient, validator);
    }

    @Test
    void whenRequestSuccessful_ReturnNonEmptyAccessToken() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(tokenResponse)));

        Result<SpotifyTokenResponse> response = sendTokenRequest();

        assertThat(response.data().accessToken())
                .isNotBlank()
                .isNotNull();
    }

    @Test
    void whenRequestTimeout_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withFixedDelay(7000)));

        assertThrows(ApiRequestTimeoutException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith401Error_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"Unauthorized\"")));

        assertThrows(ApiAuthenticationException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith429Error_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"To many requests\"")));

        assertThrows(ApiTooManyRequestsException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith400Error_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"Bad request\"")));

        assertThrows(ApiBadRequestException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith5xxError_ThrowException() {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"message\": \"Service unavailable\"")));

        assertThrows(ApiServerErrorException.class, this::sendTokenRequest);
    }

    private Result<SpotifyTokenResponse> sendTokenRequest() {
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", "code", "http://localhost:8080/callback");
        return tokenApi.fetchAccessToken(request);
    }
}
