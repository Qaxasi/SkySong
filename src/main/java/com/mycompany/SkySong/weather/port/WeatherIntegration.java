package com.mycompany.SkySong.weather.port;

import com.mycompany.SkySong.weather.model.Weather;

public interface WeatherIntegration {
    Weather fetchWeatherData(double lat, double lon);
}
