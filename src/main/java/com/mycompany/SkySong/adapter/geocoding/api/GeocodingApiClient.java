package com.mycompany.SkySong.adapter.geocoding.api;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.geocoding.dto.GeocodingResponse;
import com.mycompany.SkySong.adapter.geocoding.dto.Coordinates;
import com.mycompany.SkySong.domain.geocoding.model.Location;
import com.mycompany.SkySong.domain.geocoding.port.GeocodingIntegration;
import com.mycompany.SkySong.shared.utils.ErrorType;
import com.mycompany.SkySong.shared.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@Slf4j
public class GeocodingApiClient implements GeocodingIntegration {

    private final String apiKey;
    private final WebClient webClient;

    public GeocodingApiClient(@Qualifier("geocodingWebClient") WebClient webClient,
                              @Value("${geocoding.api.key}") String apiKey) {
        this.webClient = Objects.requireNonNull(webClient, "WebClient cannot be null");
        this.apiKey = Objects.requireNonNull(apiKey, "Api key cannot be null");;
    }

    @Override
    public Result<Location> getCoordinates(String address) {
        GeocodingResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("text", address)
                        .queryParam("apiKey", apiKey)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    log.error("Exceeded number of allowed calls to Geocoding API: {}", res.statusCode());
                    throw new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Geocoding API. Please try again later.");
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    log.error("Invalid authorization token: {}", res.statusCode());
                    throw new ApiAuthenticationException("Invalid authorization token.");
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    log.error("Server is unavailable: {}", res.statusCode());
                    throw new ServiceUnavailableException(
                            "Failed to fetch geocoding data. Please try again later.");
                })
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, res -> {
                    log.error("An error occurred while fetching geocoding data: {}", res.statusCode());
                    throw new InternalServerErrorException(
                            "An error occurred while fetching geocoding data.");
                })
                .bodyToMono(GeocodingResponse.class)
                .block();

        return validateAndExtractCoordinates(response)
                .map(result -> new Location(result.lat(), result.lon()));
    }

    private Result<Coordinates> validateAndExtractCoordinates(GeocodingResponse response) {
        if (response == null || response.results() == null || response.results().isEmpty()) {
            return Result.failure("The specified location could not be found in our data source.", ErrorType.UNPROCESSABLE_ENTITY);
        }
        return Result.success(response.results().get(0));
    }
}