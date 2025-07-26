package com.mycompany.SkySong.weather.domain.port;

import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.weather.domain.model.Weather;

public interface WeatherIntegration {
    Result<Weather> fetchWeatherData(double lat, double lon);
}
