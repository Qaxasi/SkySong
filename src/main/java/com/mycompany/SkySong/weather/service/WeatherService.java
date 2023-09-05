package com.mycompany.SkySong.weather.service;

import com.mycompany.SkySong.weather.entity.WeatherInfo;

public interface WeatherService {
    WeatherInfo getCurrentWeatherByLocationName(String locationName);
}
