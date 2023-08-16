package com.mycompany.SkySong.repository;

import com.mycompany.SkySong.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDAO extends JpaRepository<Weather, Integer> {
    Weather findWeatherByLocalityName(String localityName);
}
