package com.mycompany.SkySong.adapter.weather.api;

import com.mycompany.SkySong.adapter.exception.common.*;
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
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, res -> {
                    log.error("Exceeded number of allowed calls to Weather API: {}", res.statusCode());
                    throw new ApiTooManyRequestsException(
                            "Exceeded number of allowed calls to Weather API. Please try again later.");
                })
                .onStatus(HttpStatus.UNAUTHORIZED::equals, res -> {
                    log.error("Invalid authorization token: {}", res.statusCode());
                    throw new ApiAuthenticationException("Invalid authorization token.");
                })
                .onStatus(HttpStatus.SERVICE_UNAVAILABLE::equals, res -> {
                    log.error("Server is unavailable: {}", res.statusCode());
                    throw new ServiceUnavailableException(
                            "Failed to fetch weather data. Please try again later.");
                })
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, res -> {
                    log.error("An error occurred while fetching weather data: {}", res.statusCode());
                    throw new InternalServerErrorException(
                            "An error occurred while fetching weather data.");
                })
                .bodyToMono(WeatherApiResponse.class)
                .onErrorMap(TimeoutException.class, ex ->
                        new ApiRequestTimeoutException("The request timed out. Please check your connection and try again."))
                .block();

        return validateWeatherResponse(response)
                .map(mapper::mapToModel);
    }

    private Result<WeatherApiResponse> validateWeatherResponse(WeatherApiResponse weather) {
        if (weather == null || weather.isIncomplete()) {
            log.warn("Weather API response is missing required data");
            return Result.failure("Weather information could not be processed due to missing data.", ErrorType.UNPROCESSABLE_ENTITY);
        }
        return Result.success(weather);
    }
}
