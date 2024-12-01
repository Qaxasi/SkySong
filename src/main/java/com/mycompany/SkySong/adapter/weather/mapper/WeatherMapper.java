package com.mycompany.SkySong.adapter.weather.mapper;

import com.mycompany.SkySong.adapter.weather.dto.WeatherApiResponse;
import com.mycompany.SkySong.adapter.weather.dto.WeatherType;
import com.mycompany.SkySong.domain.weather.model.Weather;

public class WeatherMapper {
    public Weather mapToModel(WeatherApiResponse weather) {
        return new Weather(
                weather.atmosphericConditions().temperature(),
                weather.atmosphericConditions().humidity(),
                weather.clouds().cloudCoverage(),
                weather.wind().speed(),
                weather.daytime().sunrise(),
                weather.daytime().sunset(),
                weather.conditions().stream()
                        .map(WeatherType::condition)
                        .toList()
        );
    }
}
