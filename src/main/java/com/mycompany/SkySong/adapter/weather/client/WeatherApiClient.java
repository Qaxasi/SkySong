package com.mycompany.SkySong.adapter.weather.api;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.adapter.weather.dto.WeatherApiResponse;
import com.mycompany.SkySong.adapter.weather.mapper.WeatherMapper;
import com.mycompany.SkySong.domain.weather.model.Weather;
import com.mycompany.SkySong.domain.weather.port.WeatherIntegration;
import com.mycompany.SkySong.shared.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class WeatherApiClient implements WeatherIntegration {
    private final String apiKey;
    private final WebClient webClient;
    private final WeatherMapper mapper;

    public WeatherApiClient(@Value("${weather.api.key}") String apiKey,
                            @Qualifier("weatherWebClient") WebClient webClient,
                            WeatherMapper mapper) {
        this.apiKey = Objects.requireNonNull(apiKey, "Api key cannot be null");
        this.webClient = Objects.requireNonNull(webClient, "WebClient cannot be null");
        this.mapper = mapper;
    }

    @Override
    public Weather fetchWeatherData(double lat, double lon) {
        log.debug("[Weather API] Requesting weather data for lat={}, lon={}", lat, lon);

        WeatherApiResponse response = fetchFromApi(lat, lon);
        log.debug("[Weather API] Response received for lat={}, lon={}", lat, lon);

        return validateAndMapToDomain(response);
    }

    private Weather validateAndMapToDomain(WeatherApiResponse response) {
        validateWeatherResponse(response);
        return mapper.mapToModel(response);
    }

    private WeatherApiResponse fetchFromApi(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, res -> {
                    log.warn("[Weather API] Received Bad request for coordinates (lat={}, lon={}) - status: {}", lat, lon,  res.statusCode());
                    return Mono.error(new ApiBadRequestException(
                            "The provided coordinates could not be processed. Please check and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST));
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, res -> {
                    log.error("[Weather API] Access forbidden - status: {}", res.statusCode());
                    return Mono.error(new ApiForbiddenException(
                            "You are not authorized to access weather data.",
                            ErrorType.EXTERNAL_API_FORBIDDEN));
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    log.error("[Weather API] Rate limit exceeded - status: {}", res.statusCode());
                    return Mono.error(new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Weather API. Please try again later.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT));
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    log.error("[Weather API] Unauthorized access - invalid API key - status: {}", res.statusCode());
                    return Mono.error(new ApiAuthenticationException(
                            "Weather API authentication failed. Please verify your API key.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED));
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    log.error("[Weather API] Service unavailable - status: {}", res.statusCode());
                    return Mono.error(new ApiServerErrorException(
                            "Weather service is temporarily unavailable. Please try again shortly.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR));
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    log.error("[Weather API] Unexpected server error - status: {}", res.statusCode());
                    return Mono.error(new ApiServerErrorException(
                            "A server error occurred while retrieving weather data. Please try again soon.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR));
                })
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    log.error("[Weather API] Unexpected client error - status: {}", res.statusCode());
                    return Mono.error(new ApiClientErrorException(
                            "The request could not be processed due to a client-side error. Please verify request parameters.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR));
                })
                .bodyToMono(WeatherApiResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    log.error("[Weather API] Request timed out", ex);
                    return new ApiRequestTimeoutException(
                            "The request timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_TIMEOUT);
                })
                .block();
    }

    private void validateWeatherResponse(WeatherApiResponse response) {
        if (response == null) {
            log.warn("[Weather API] Null response received from API.");
            throw new ApiResponseException(
                    "No response was received from the weather provider.",
                    ErrorType.WEATHER_NO_RESULTS);
        }

        if (response.isIncomplete()) {
            log.warn("[Weather API] Incomplete response received: {}", response);
            throw new ApiResponseException(
                    "The weather data is incomplete and cannot be processed",
                    ErrorType.WEATHER_INCOMPLETE_RESPONSE);
        }
    }
}
