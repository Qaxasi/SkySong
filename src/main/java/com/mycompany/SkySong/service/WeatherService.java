package com.mycompany.SkySong.service;

import com.mycompany.SkySong.dto.WeatherDto;

import java.io.IOException;

public interface WeatherService {
    WeatherDto getCurrentWeatherByLocationName(String locationName) throws IOException;



}
