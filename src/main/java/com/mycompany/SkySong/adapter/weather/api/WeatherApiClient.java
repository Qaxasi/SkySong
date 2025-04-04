package com.mycompany.SkySong.adapter.weather.api;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.adapter.weather.dto.WeatherApiResponse;
import com.mycompany.SkySong.adapter.weather.mapper.WeatherMapper;
import com.mycompany.SkySong.domain.weather.model.Weather;
import com.mycompany.SkySong.domain.weather.port.WeatherIntegration;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
    public Result<Weather> fetchWeatherData(double lat, double lon) {
        WeatherApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, res -> {
                    log.warn("[Weather API] Bad request for coordinates (lat={}, lon={}) - status: {}", lat, lon,  res.statusCode());
                    throw new ApiBadRequestException(
                            "The provided coordinates could not be processed. Please check and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST);
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, res -> {
                    log.error("[Weather API] Access forbidden - status: {}", res.statusCode());
                    throw new ApiForbiddenException(
                            "You are not authorized to access weather data.",
                            ErrorType.EXTERNAL_API_FORBIDDEN);
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    log.error("[Weather API] Rate limit exceeded - status: {}", res.statusCode());
                    throw new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Weather API. Please try again later.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT);
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    log.error("[Weather API] Unauthorized access - invalid API key - status: {}", res.statusCode());
                    throw new ApiAuthenticationException(
                            "Weather API authentication failed. Please verify your API key.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED);
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    log.error("[Weather API] Service unavailable - status: {}", res.statusCode());
                    throw new ApiServerErrorException(
                            "Weather service is temporarily unavailable. Please try again shortly.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR);
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    log.error("[Weather API] Unexpected server error - status: {}", res.statusCode());
                    throw new ApiServerErrorException(
                            "A server error occurred while retrieving weather data. Please try again soon.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR);
                })
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    log.error("[Weather API] Unexpected client error - status: {}", res.statusCode());
                    throw new ApiClientErrorException(
                            "The request could not be processed due to a client-side error. Please verify request parameters.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR);
                })
                .bodyToMono(WeatherApiResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    log.error("[Weather API] Request timed out", ex);
                    return new ApiRequestTimeoutException(
                            "The request timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_TIMEOUT);
                })
                .block();

        return validateWeatherResponse(response)
                .map(mapper::mapToModel);
    }

    private Result<WeatherApiResponse> validateWeatherResponse(WeatherApiResponse weather) {
        if (weather == null || weather.isIncomplete()) {
            log.warn("[Weather API] Validation failed - response is null or incomplete: {}", weather);
            return Result.failure(
                    "The weather data could not be processed due to missing or invalid content.",
                    ErrorType.WEATHER_DATA_INCOMPLETE);
        }
        return Result.success(weather);
    }
}
