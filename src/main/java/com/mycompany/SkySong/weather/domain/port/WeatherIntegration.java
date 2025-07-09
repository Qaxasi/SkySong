package com.mycompany.SkySong.weather.domain.port;

import com.mycompany.SkySong.weather.domain.model.Weather;

public interface WeatherIntegration {
    Weather fetchWeatherData(double lat, double lon);
}
