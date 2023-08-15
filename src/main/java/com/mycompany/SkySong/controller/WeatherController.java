package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.entity.Weather;
import com.mycompany.SkySong.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    @GetMapping("/current/{location}")
    public ResponseEntity<Weather> getCurrentWeatherForLocation(
            @PathVariable(name = "location") String location) throws IOException {
        Weather weatherForLocation = weatherService.getCurrentWeatherByCityName(location);
        return new ResponseEntity<>(weatherForLocation, HttpStatus.OK);

    }
}
