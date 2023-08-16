package com.mycompany.SkySong.service;

import com.mycompany.SkySong.entity.Weather;

import java.io.IOException;

public interface WeatherService {
    Weather getCurrentWeatherByLocalityName(String localityName) throws IOException;



}
