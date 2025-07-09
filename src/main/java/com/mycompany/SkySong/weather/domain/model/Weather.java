package com.mycompany.SkySong.weather.domain.model;

public record Weather (
        double temperature,
        int humidity,
        int cloudCoverage,
        double windSpeed,
        double rainVolume,
        double snowVolume,
        int sunrise,
        int sunset
) {
}
