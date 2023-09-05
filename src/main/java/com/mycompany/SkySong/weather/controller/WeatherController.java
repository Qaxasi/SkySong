package com.mycompany.SkySong.weather.WeatherController;

import com.mycompany.SkySong.weather.WeatherEntity.WeatherInfo;
import com.mycompany.SkySong.weather.WeatherService.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather/current")
public class WeatherController {
    private WeatherService weatherService;
    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{locationName}")
    public WeatherInfo getWeatherForLocation(@PathVariable String locationName) {
        return weatherService.getCurrentWeatherByLocationName(locationName);
    }
}
