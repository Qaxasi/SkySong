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
    private String response;

    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/spotify/auth")
                .build();

        response = JsonFileLoader.loadJson("spotify_access_token_response.json");

        SpotifyAccessTokenValidator validator = new SpotifyAccessTokenValidator();
        tokenApi = new SpotifyAccessTokenApi("clientId", "clientSecret", webClient, validator);
    }

    @Test
    void whenRequestSuccessful_ReturnAccessToken() {
        mockApiResponse(200, response);

        Result<SpotifyTokenResponse> result = sendTokenRequest();

        assertThat(result.data().accessToken())
                .isNotBlank()
                .isNotNull();
    }

    @Test
    void whenRequestSuccessful_ReturnRefreshToken() {
        mockApiResponse(200, response);

        Result<SpotifyTokenResponse> result = sendTokenRequest();

        assertThat(result.data().refreshToken())
                .isNotBlank()
                .isNotNull();
    }

    @Test
    void whenRequestSuccessful_ReturnScope() {
        mockApiResponse(200, response);

        Result<SpotifyTokenResponse> result = sendTokenRequest();

        assertThat(result.data().scope())
                .isNotBlank()
                .isNotNull();
    }

    @Test
    void whenRequestFailsWith401Error_ThrowException() {
        mockApiResponse(401, "\"message\": \"Unauthorized\"");

        assertThrows(ApiAuthenticationException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith429Error_ThrowException() {
        mockApiResponse(429, "\"message\": \"To many requests\"");

        assertThrows(ApiTooManyRequestsException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith400Error_ThrowException() {
        mockApiResponse(400, "\"message\": \"Bad request\"");

        assertThrows(ApiBadRequestException.class, this::sendTokenRequest);
    }

    @Test
    void whenRequestFailsWith5xxError_ThrowException() {
        mockApiResponse(503, "\"message\": \"Service unavailable\"");

        assertThrows(ApiServerErrorException.class, this::sendTokenRequest);
    }

    private void mockApiResponse(int statusCode, String response) {
        wireMockServer.stubFor(post("/v1/spotify/auth/api/token")
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)));
    }

    private Result<SpotifyTokenResponse> sendTokenRequest() {
        SpotifyAccessTokenRequest request = new SpotifyAccessTokenRequest(
                "authorization_code", "code", "http://localhost:8080/callback");
        return tokenApi.fetchAccessToken(request);
    }
}
