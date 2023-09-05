package com.mycompany.SkySong.weather.repository;

import com.mycompany.SkySong.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDAO extends JpaRepository<Weather, Integer> {
    Weather findWeatherByLocationName(String locationName);
}
