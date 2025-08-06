package com.mycompany.SkySong.weather.adapter.out.client;

import com.mycompany.SkySong.adapter.exception.external.*;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.weather.adapter.out.dto.WeatherApiResponse;
import com.mycompany.SkySong.weather.adapter.out.mapper.WeatherMapper;
import com.mycompany.SkySong.weather.domain.model.Weather;
import com.mycompany.SkySong.weather.domain.port.WeatherIntegration;
import com.mycompany.SkySong.shared.error.ErrorType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

@Service
public class WeatherApiClient implements WeatherIntegration {
    private final String apiKey;
    private final WebClient webClient;
    private final WeatherApiResponseValidator validator;
    private final WeatherMapper mapper;
    private final ApplicationLogger logger;

    public WeatherApiClient(@Value("${weather.api.key}") final String apiKey,
                            @Qualifier("weatherWebClient") final WebClient webClient,
                            final WeatherApiResponseValidator validator,
                            final WeatherMapper mapper,
                            final ApplicationLogger logger) {
        this.apiKey = apiKey;
        this.webClient = webClient;
        this.validator = validator;
        this.mapper = mapper;
        this.logger = logger;
    }

    @Override
    public Result<Weather> fetchWeatherData(final double lat, final double lon) {
        logger.debug("[Weather API] Requesting weather data for",
                context(Map.of("lat", lat, "lon", lon)));

        try {
            final WeatherApiResponse response = fetchFromApi(lat, lon);

            return validator.validateWeatherResponse(response)
                    .onFailure(error -> logger.warn("[Weather API] Weather data validation failed",
                            context(Map.of("lat", lat, "lon", lon, "errorType", error.errorType()))))
                    .map(ignored -> mapper.mapToDomain(response));
        } catch (ExternalApiException e) {
            return Result.failure(e.getMessage(), e.getErrorType());
        }
    }

    private WeatherApiResponse fetchFromApi(final double lat, final double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, res -> {
                    logger.warn("[Weather API] Received Bad request",
                            context(Map.of("lat", lat, "lon", lon, "statusCode", res.statusCode())));
                    return Mono.error(new ExternalApiHttpException(
                            "The provided coordinates could not be processed. Please check and try again.",
                            ErrorType.EXTERNAL_API_BAD_REQUEST));
                })
                .onStatus(HttpStatus.FORBIDDEN::equals, res -> {
                    logger.error("[Weather API] Access forbidden",
                            context("status", res.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "You are not authorized to access weather data.",
                            ErrorType.EXTERNAL_API_FORBIDDEN));
                })
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    logger.error("[Weather API] Rate limit exceeded",
                            context("status", res.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "Exceeded number of allowed calls to Weather API. Please try again later.",
                            ErrorType.EXTERNAL_API_RATE_LIMIT));
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    logger.error("[Weather API] Unauthorized access - invalid API key",
                            context("status", res.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "Weather API authentication failed. Please verify your API key.",
                            ErrorType.EXTERNAL_API_UNAUTHORIZED));
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    logger.error("[Weather API] Service unavailable",
                            context("status", res.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "Weather service is temporarily unavailable. Please try again shortly.",
                            ErrorType.EXTERNAL_API_SERVICE_UNAVAILABLE));
                })
                .onStatus(HttpStatusCode::is5xxServerError, res -> {
                    logger.error("[Weather API] Unexpected server error",
                            context("status",res.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "A server error occurred while retrieving weather data. Please try again soon.",
                            ErrorType.EXTERNAL_API_SERVER_ERROR));
                })
                .onStatus(HttpStatusCode::is4xxClientError, res -> {
                    logger.error("[Weather API] Unexpected client error",
                            context("status", res.statusCode()));
                    return Mono.error(new ExternalApiHttpException(
                            "The request could not be processed due to a client-side error. Please verify request parameters.",
                            ErrorType.EXTERNAL_API_CLIENT_ERROR));
                })
                .bodyToMono(WeatherApiResponse.class)
                .onErrorMap(TimeoutException.class, ex -> {
                    logger.error("[Weather API] Request timed out", ex);
                    return new ExternalApiTimeoutException(
                            "The request timed out. Please check your connection and try again.",
                            ErrorType.EXTERNAL_API_TIMEOUT);
                })
                .block();
    }
}