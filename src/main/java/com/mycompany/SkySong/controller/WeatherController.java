package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.entity.WeatherInfo;
import com.mycompany.SkySong.entity.WeatherRequest;
import com.mycompany.SkySong.service.WeatherService;
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
