package com.mycompany.SkySong.adapter.weather.mapper;

import com.mycompany.SkySong.adapter.weather.dto.WeatherApiResponse;
import com.mycompany.SkySong.adapter.weather.dto.WeatherType;
import com.mycompany.SkySong.domain.weather.model.Weather;

public class WeatherMapper {
    public Weather mapToModel(WeatherApiResponse weather) {
        double rainVolume = weather.rain() != null ? weather.rain().rainVolume() : 0.0;
        double snowVolume = weather.snow() != null ? weather.snow().snowVolume() : 0.0;

        return new Weather(
                weather.atmosphericConditions().temperature(),
                weather.atmosphericConditions().humidity(),
                weather.clouds().cloudCoverage(),
                weather.wind().speed(),
                rainVolume,
                snowVolume,
                weather.daytime().sunrise(),
                weather.daytime().sunset(),
                weather.conditions().stream()
                        .map(WeatherType::condition)
                        .toList()
        );
    }
}
