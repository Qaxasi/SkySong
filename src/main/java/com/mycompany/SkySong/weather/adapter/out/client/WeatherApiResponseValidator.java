package com.mycompany.SkySong.weather.adapter.out.client;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.weather.adapter.out.dto.WeatherApiResponse;

public class WeatherApiResponseValidator {
    public Result<Void> validateWeatherResponse(final WeatherApiResponse response) {
        if (response == null) {
            return Result.failure("No response was received from the weather provider", ErrorType.WEATHER_NO_RESULTS);
        }
        if (!isComplete(response)) {
            return Result.failure("The weather data is incomplete and cannot be processed", ErrorType.WEATHER_INCOMPLETE_RESPONSE);
        }
        if (!hasInvalidValue(response)) {
            return Result.failure("The weather data contains invalid values and cannot be processed", ErrorType.WEATHER_INVALID_VALUES);
        }
        return Result.success();
    }

    private boolean isComplete(final WeatherApiResponse response) {
        return response != null &&
                response.atmosphericConditions() != null &&
                response.clouds() != null &&
                response.wind() != null &&
                response.daytime() != null &&
                response.atmosphericConditions().temperature() != null &&
                response.atmosphericConditions().humidity() != null &&
                response.clouds().cloudCoverage() != null &&
                response.wind().speed() != null &&
                response.daytime().sunrise() != null &&
                response.daytime().sunset() != null;
    }

    private boolean hasInvalidValue(final WeatherApiResponse response) {
        return response.atmosphericConditions().temperature() < -90
                || response.atmosphericConditions().temperature() > 60
                || response.atmosphericConditions().humidity() < 0
                || response.atmosphericConditions().humidity() > 100
                || response.clouds().cloudCoverage() < 0
                || response.clouds().cloudCoverage() > 100
                || response.wind().speed() < 0
                || response.wind().speed() > 150
                || response.daytime().sunset()  <= 0
                || response.daytime().sunrise() <= 0
                || response.daytime().sunset() <= response.daytime().sunrise();
    }
}
