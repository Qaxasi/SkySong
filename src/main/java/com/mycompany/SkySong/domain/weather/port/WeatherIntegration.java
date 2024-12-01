package com.mycompany.SkySong.domain.weather.port;

import com.mycompany.SkySong.domain.weather.model.Weather;
import com.mycompany.SkySong.shared.utils.Result;

public interface WeatherIntegration {
    Result<Weather> fetchWeatherData(double lat, double lon);
}
