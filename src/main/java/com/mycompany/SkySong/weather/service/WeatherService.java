package com.mycompany.SkySong.weather.WeatherService;

import com.mycompany.SkySong.weather.WeatherEntity.WeatherInfo;

public interface WeatherService {
    WeatherInfo getCurrentWeatherByLocationName(String locationName);
}
