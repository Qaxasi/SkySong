package com.mycompany.SkySong.adapter.geocoding.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.adapter.geocoding.dto.GeocodingResponse;
import com.mycompany.SkySong.adapter.geocoding.dto.Coordinates;
import com.mycompany.SkySong.domain.geocoding.model.Location;
import com.mycompany.SkySong.domain.geocoding.port.GeocodingIntegration;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.concurrent.TimeoutException;

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
                .onStatus(HttpStatus.BAD_REQUEST::equals, res -> {
                    log.warn("Invalid address format: {}", res.statusCode());
                    throw new ApiBadRequestException(
                            "Invalid address format. Please correct it.", ErrorType.EXTERNAL_API_BAD_REQUEST);
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, res -> {
                    log.error("Access denied: {}", res.statusCode());
                    throw new ApiForbiddenException(
                            "You are not authorized to access geolocation data.", ErrorType.EXTERNAL_API_FORBIDDEN);
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    log.error("Exceeded number of allowed calls to Geocoding API: {}", res.statusCode());
                    throw new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Geocoding API. Please wait a moment and try again.", ErrorType.EXTERNAL_API_RATE_LIMIT);
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    log.error("Invalid authorization token: {}", res.statusCode());
                    throw new ApiAuthenticationException(
                            "Authentication with Geocoding API failed. Please verify your API credentials.", ErrorType.EXTERNAL_API_UNAUTHORIZED );
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    log.error("Geocoding service is unavailable: {}", res.statusCode());
                    throw new ApiServerErrorException(
                            "Geocoding service is temporarily unavailable. Please try again shortly.", ErrorType.EXTERNAL_API_UNAVAILABLE);
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    log.error("Unexpected server error from Geocoding API: {}", res.statusCode());
                    throw new ApiServerErrorException(
                            "An error occurred while fetching geocoding data.", ErrorType.EXTERNAL_API_SERVER_ERROR);
                })
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    log.error("Unexpected client error from Geocoding API: {}", res.statusCode());
                    throw new ApiClientErrorException(
                            "The request could not be processed due to a client-side error. Please verify request parameters.", ErrorType.EXTERNAL_API_CLIENT_ERROR);
                })
                .bodyToMono(GeocodingResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    log.error("Geocoding request timeout", ex);
                    return new ApiRequestTimeoutException(
                            "The request timed out. Please check your connection and try again.", ErrorType.EXTERNAL_API_TIMEOUT);
                })
                .block();

        return validateAndExtractCoordinates(response)
                .map(coords -> new Location(coords.lat(), coords.lon()));
    }

    private Result<Coordinates> validateAndExtractCoordinates(GeocodingResponse response) {
        if (response == null || response.isIncomplete()) {
            log.warn("Empty or null geocoding response received");
            return Result.failure("The specified location could not be found in our data source.", ErrorType.UNPROCESSABLE_ENTITY);
        }

        Coordinates coordinates = response.results().get(0);

        if (!coordinates.isValidCoordinate()) {
            log.warn("Received invalid coordinates: {}", coordinates);
            return Result.failure("Received invalid coordinates from geocoding provider", ErrorType.UNPROCESSABLE_ENTITY);
        }

        return Result.success(coordinates);
    }
}