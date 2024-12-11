package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.api;

import com.mycompany.SkySong.adapter.exception.common.ApiAuthenticationException;
import com.mycompany.SkySong.adapter.exception.common.ApiBadRequestException;
import com.mycompany.SkySong.adapter.exception.common.ApiServerErrorException;
import com.mycompany.SkySong.adapter.exception.common.ApiTooManyRequestsException;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyRefreshTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.adapter.spotify.authentication.validation.SpotifyRefreshTokenValidator;
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

class SpotifyRefreshTokenApiTest extends BaseWireMock {

    private SpotifyRefreshTokenApi tokenApi;
    private String response;

    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/spotify")
                .build();

        response = JsonFileLoader.loadJson("spotify_refresh_token_response.json");

        SpotifyRefreshTokenValidator validator = new SpotifyRefreshTokenValidator();
        tokenApi = new SpotifyRefreshTokenApi("spotifyClientId", "spotifyClientSecret", webClient, validator);
    }

    @Test
    void whenRequestSuccessful_ReturnAccessToken() {
        mockApiResponse(200, response);

        Result<SpotifyTokenResponse> result = sendTokenRequest();

        assertThat(result.data().refreshToken())
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
        wireMockServer.stubFor(post("/v1/spotify/api/token")
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)));
    }

    private Result<SpotifyTokenResponse> sendTokenRequest() {
        SpotifyRefreshTokenRequest request = new SpotifyRefreshTokenRequest("grant_type", "refresh_token");
        return tokenApi.fetchRefreshToken(request);
    }
}
