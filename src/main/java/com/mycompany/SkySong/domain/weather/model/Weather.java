package com.mycompany.SkySong.domain.weather.model;

import java.util.List;

public record Weather (
        double temperature,
        int humidity,
        int cloudCoverage,
        double windSpeed,
        double rainVolume,

        int sunrise,
        int sunset,
        List<String> conditions
) {
}
