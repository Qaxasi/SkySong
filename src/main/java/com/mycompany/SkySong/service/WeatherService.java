package com.mycompany.SkySong.service;

import com.mycompany.SkySong.entity.WeatherInfo;
import com.mycompany.SkySong.entity.WeatherRequest;

public interface WeatherService {
    WeatherInfo getCurrentWeatherByLocationName(String locationName);
}
