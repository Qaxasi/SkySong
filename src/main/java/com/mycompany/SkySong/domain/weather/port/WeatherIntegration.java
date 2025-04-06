package com.mycompany.SkySong.domain.weather.port;

import com.mycompany.SkySong.domain.weather.model.Weather;
import com.mycompany.SkySong.shared.result.Result;

public interface WeatherIntegration {
    Weather fetchWeatherData(double lat, double lon);
}
