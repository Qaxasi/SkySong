package com.mycompany.SkySong.weather.adapter.out.mapper;

import com.mycompany.SkySong.weather.adapter.out.dto.WeatherApiResponse;
import com.mycompany.SkySong.weather.domain.model.Weather;

public class WeatherMapper {
    public Weather mapToModel(final WeatherApiResponse weather) {
        final double rainVolume = weather.rain() != null ? weather.rain().rainVolume() : 0.0;
        final double snowVolume = weather.snow() != null ? weather.snow().snowVolume() : 0.0;

        return new Weather(
                weather.atmosphericConditions().temperature(),
                weather.atmosphericConditions().humidity(),
                weather.clouds().cloudCoverage(),
                weather.wind().speed(),
                rainVolume,
                snowVolume,
                weather.daytime().sunrise(),
                weather.daytime().sunset()
        );
    }
}
