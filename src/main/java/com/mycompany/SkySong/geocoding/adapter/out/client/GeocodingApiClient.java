package com.mycompany.SkySong.geocoding.adapter.out.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.geocoding.adapter.out.dto.GeocodingApiResponse;
import com.mycompany.SkySong.geocoding.domain.model.Coordinates;
import com.mycompany.SkySong.geocoding.domain.port.GeocodingIntegration;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Service
public class GeocodingApiClient implements GeocodingIntegration {
    private final String apiKey;
    private final WebClient webClient;
    private final ApplicationLogger logger;

    public GeocodingApiClient(@Qualifier("geocodingWebClient") final WebClient webClient,
                              @Value("${geocoding.api.key}") final String apiKey,
                              final ApplicationLogger logger) {
        this.webClient = Objects.requireNonNull(webClient, "WebClient cannot be null");
        this.apiKey = Objects.requireNonNull(apiKey, "Api key cannot be null");
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null");
    }

    @Override
    public Coordinates fetchCoordinates(final String address) {
        logger.debug("[Geocoding API] Requesting geocoding data", context("address", address));
        GeocodingApiResponse response = fetchFromApi(address);
        logger.debug("[Geocoding API] Response received", context("address", address));

        validateResponse(response);
        return mapToDomain(response);
    }

    private GeocodingApiResponse fetchFromApi(final String address) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("text", address)
                        .queryParam("apiKey", apiKey)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, res -> {
                    logger.warn("[Geocoding API] Received Bad request", context(Map.of("address", address, "status", res.statusCode())));
                    return Mono.error(new ApiBadRequestException(
                            "The provided address could not be processed. Please check the spelling and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST));
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, res -> {
                    logger.error("[Geocoding API] Access forbidden", context("status", res.statusCode()));
                    return Mono.error(new ApiForbiddenException(
                            "You are not authorized to access geolocation data.",
                            ErrorType.EXTERNAL_API_FORBIDDEN));
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    logger.error("[Geocoding API] Rate limit exceeded", context("status", res.statusCode()));
                    return Mono.error(new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Geocoding API. Please wait a moment and try again.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT));
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    logger.error("[Geocoding API] Unauthorized access - invalid API key", context("status", res.statusCode()));
                    return Mono.error(new ApiAuthenticationException(
                            "Authentication with Geocoding API failed. Please verify your API credentials.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED ));
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    logger.error("[Geocoding API] Service unavailable", context("status", res.statusCode()));
                    return Mono.error(new ApiServerErrorException(
                            "Geocoding service is temporarily unavailable. Please try again shortly.",
                            ErrorType.EXTERNAL_API_UNAVAILABLE));
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    logger.error("[Geocoding API] Unexpected server error", context("status", res.statusCode()));
                    return Mono.error(new ApiServerErrorException(
                            "A server error occurred while retrieving geolocation data. Please try again soon.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR));
                })
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    logger.error("[Geocoding API] Unexpected client error", context("status", res.statusCode()));
                    return Mono.error(new ApiClientErrorException(
                            "The request could not be processed due to a client-side error. Please verify request parameters.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR));
                })
                .bodyToMono(GeocodingApiResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    logger.error("[Geocoding API] Request timed out", ex);
                    return new ApiRequestTimeoutException(
                            "The request timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_TIMEOUT);
                })
                .block();
    }

    private void validateResponse(final GeocodingApiResponse response) {
        if (response == null) {
            logger.warn("[Geocoding API] Null response received from API.", context("response", response));
            throw new ApiResponseException(
                    "No response was received from the geolocation provider.",
                    ErrorType.GEOCODING_NO_RESULTS);
        }

        if (response.results() == null || response.results().isEmpty()) {
            logger.warn("[Geocoding API] Empty results in response", context("response", response));
            throw new ApiResponseException(
                    "No coordinates found for specific address",
                    ErrorType.GEOCODING_NO_RESULTS);
        }

        if (!containsOnlyValidCoordinates(response)) {
            logger.warn("[Geocoding API] Invalid coordinates in response", context("response", response));
            throw new ApiResponseException(
                    "Geocoding API returned coordinates outside valid range",
                    ErrorType.GEOCODING_INVALID_COORDINATES);

        }
    }

    private boolean containsOnlyValidCoordinates(final GeocodingApiResponse response) {
        return response.results().stream().allMatch(cord ->
                cord.lat() >= -90 && cord.lat() <= 90 &&
                        cord.lon() >= -180 && cord.lon() <= 180);
    }
    
    private Coordinates mapToDomain(final GeocodingApiResponse response) {
        return response.results().stream()
                .findFirst()
                .map(c -> new Coordinates(c.lat(), c.lon()))
                .orElseThrow(() -> new ApiResponseException("No valid coordinates found in geocoding response", ErrorType.GEOCODING_NO_RESULTS));
    }
}