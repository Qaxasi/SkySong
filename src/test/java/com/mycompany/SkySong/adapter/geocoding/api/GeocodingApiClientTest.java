package com.mycompany.SkySong.adapter.geocoding.api;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.geocoding.dto.GeocodingResult;
import com.mycompany.SkySong.testutils.common.BaseWireMock;
import com.mycompany.SkySong.testutils.utils.JsonFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeocodingApiClientTest extends BaseWireMock {

    private GeocodingApiClient geocodingApi;
    private String geocodingResponse;

    @BeforeEach
    void setup() throws IOException {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/v1/geocode")
                .build();

        geocodingResponse = JsonFileLoader.loadJson("geocoding_response.json");

        geocodingApi = new GeocodingApiClient(webClient, "test-api-key");
    }

    @Test
    void whenGeocodingIsSuccessful_ReturnCorrectCoordinates() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("Warsaw 00-001"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(geocodingResponse)));

        GeocodingResult result = fetchGeocodingData("Warsaw 00-001");


        assertThat(result.lat()).isEqualTo(52.22985215);
        assertThat(result.lon()).isEqualTo(21.006524862);
    }

    @Test
    void whenInvalidApiKeyProvided_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("Warsaw 00-001"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")));

        assertThrows(AuthorizationException.class, () -> fetchGeocodingData("Warsaw 00-001"));
    }

    @Test
    void whenServerReturnsToManyRequests_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("Warsaw 00-001"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader("Content-Type", "application/json")));

        assertThrows(TooManyRequestsException.class, () -> fetchGeocodingData("Warsaw 00-001"));
    }

    @Test
    void whenServerReturnsServiceUnavailable_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("Warsaw 00-001"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")));

        assertThrows(ServiceUnavailableException.class, () -> fetchGeocodingData("Warsaw 00-001"));
    }

    @Test
    void whenServerReturnsInternalError_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("Warsaw 00-001"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")));

        assertThrows(InternalServerErrorException.class, () -> fetchGeocodingData("Warsaw 00-001"));
    }

    @Test
    void whenNoResultsFound_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("NonexistentLocation"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"results\": []}")));

        assertThrows(DataNotFoundException.class, () -> fetchGeocodingData("NonexistentLocation"));
    }

    @Test
    void whenRequestTimeout_ClientThrowsException() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/geocode"))
                .withQueryParam("text", equalTo("Warsaw 00-001"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("apiKey", equalTo("test-api-key"))
                .withQueryParam("limit", equalTo("1"))
                .willReturn(aResponse()
                        .withFixedDelay(6000)
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(geocodingResponse)));

        assertThrows(WebClientTimeoutException.class,  () -> fetchGeocodingData("Warsaw 00-001"));

    }

    private GeocodingResult fetchGeocodingData(String locationName) {
        return geocodingApi.fetchGeocodingData(locationName);
    }
}
