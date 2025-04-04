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
                    log.warn("[Geocoding API] Bad request for address: '{}' - status: {}", address, res.statusCode());
                    throw new ApiBadRequestException(
                            "The provided address could not be processed. Please check the spelling and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST);
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, res -> {
                    log.error("[Geocoding API] Access forbidden - status: {}", res.statusCode());
                    throw new ApiForbiddenException(
                            "You are not authorized to access geolocation data.",
                            ErrorType.EXTERNAL_API_FORBIDDEN);
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    log.error("[Geocoding API] Rate limit exceeded - status: {}", res.statusCode());
                    throw new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Geocoding API. Please wait a moment and try again.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT);
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    log.error("[Geocoding API] Unauthorized access - invalid API key - status: {}", res.statusCode());
                    throw new ApiAuthenticationException(
                            "Authentication with Geocoding API failed. Please verify your API credentials.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED );
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    log.error("[Geocoding API] Service unavailable - status: {}", res.statusCode());
                    throw new ApiServerErrorException(
                            "Geocoding service is temporarily unavailable. Please try again shortly.",
                            ErrorType.EXTERNAL_API_UNAVAILABLE);
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    log.error("[Geocoding API] Unexpected server error - status: {}", res.statusCode());
                    throw new ApiServerErrorException(
                            "A server error occurred while retrieving geolocation data. Please try again soon.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR);
                })
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    log.error("[Geocoding API] Unexpected client error - status: {}", res.statusCode());
                    throw new ApiClientErrorException(
                            "The request could not be processed due to a client-side error. Please verify request parameters.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR);
                })
                .bodyToMono(GeocodingResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    log.error("[Geocoding API] Request timed out", ex);
                    return new ApiRequestTimeoutException(
                            "The request timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_TIMEOUT);
                })
                .block();

        return validateAndExtractCoordinates(response)
                .map(coords -> new Location(coords.lat(), coords.lon()));
    }

    private Result<Coordinates> validateAndExtractCoordinates(GeocodingResponse response) {
        if (response == null) {
            log.warn("[Geocoding API] Null response received from API.");
            return Result.failure(
                    "No response was received from the geolocation provider.",
                    ErrorType.GEOCODING_NO_RESULTS);
        }

        if (response.isIncomplete()) {
            log.warn("[Geocoding API] Incomplete response received: {}", response);
            return Result.failure(
                    "The geolocation data is incomplete and cannot be processed.",
                    ErrorType.GEOCODING_INCOMPLETE_RESPONSE);

        }

        Coordinates coordinates = response.results().get(0);

        if (!coordinates.isValidCoordinate()) {
            log.warn("[Geocoding API] Invalid coordinates received: {}", coordinates);
            return Result.failure(
                    "The geolocation service returned invalid coordinates.",
                    ErrorType.GEOCODING_INVALID_COORDINATES);
        }

        return Result.success(coordinates);
    }
}