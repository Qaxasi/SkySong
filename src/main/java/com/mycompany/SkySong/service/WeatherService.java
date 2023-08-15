package com.mycompany.SkySong.service;

import com.mycompany.SkySong.entity.Weather;

import java.io.IOException;

public interface WeatherService {
    Weather getCurrentWeatherByCityName(String cityName) throws IOException;



}
