package com.mycompany.SkySong.weather.WeatherRepository;

import com.mycompany.SkySong.weather.WeatherEntity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDAO extends JpaRepository<Weather, Integer> {
    Weather findWeatherByLocationName(String locationName);
}
