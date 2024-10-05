package com.mycompany.SkySong.adapter.spotify.authentication.api;

import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyAccessTokenRequest;
import com.mycompany.SkySong.adapter.spotify.authentication.dto.SpotifyTokenResponse;
import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.assertj.core.api.Assertions.assertThat;

class SpotifyTokenApiTest extends BaseWireMock {

    private SpotifyTokenApi spotifyTokenApi;
    private String tokenResponse;


    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/spotify/auth")
                .build();

        tokenResponse = JsonFileLoader.loadJson("spotify_access_token_response.json");

        spotifyTokenApi = new SpotifyTokenApi("clientId", "clientSecret", webClient);
    }
}
