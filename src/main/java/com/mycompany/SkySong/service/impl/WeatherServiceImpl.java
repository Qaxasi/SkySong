package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.WeatherApiClient;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.entity.Weather;
import com.mycompany.SkySong.exception.LocationNotFoundException;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.repository.WeatherDAO;
import com.mycompany.SkySong.service.WeatherService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class WeatherServiceImpl implements WeatherService {
    private WeatherDAO weatherDAO;
    private LocationDAO locationDAO;
    private WeatherApiClient weatherApiClient;

    public WeatherServiceImpl(WeatherDAO weatherDAO, LocationDAO locationDAO, WeatherApiClient weatherApiClient) {
        this.weatherDAO = weatherDAO;
        this.locationDAO = locationDAO;
        this.weatherApiClient = weatherApiClient;
    }

    @Override
    public Weather getCurrentWeatherByCityName(String cityName) throws IOException {
        Location location = findLocationByCityName(cityName);
        JsonNode rootNode = weatherApiClient.fetchWeatherData(location.getLatitude(), location.getLongitude());
        return saveOrUpdateWeatherData(rootNode, cityName);

    }

    private Location findLocationByCityName(String cityName) {
        return Optional.ofNullable(locationDAO.findByCityName(cityName))
                .orElseThrow(() -> new LocationNotFoundException("Location", "city_name", cityName));
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    private void updateAllWeatherData() throws IOException {
        List<Location> allLocations = locationDAO.findAll();
        for (Location location : allLocations) {
            getCurrentWeatherByCityName(location.getCityName());
        }
    }

    private Weather saveOrUpdateWeatherData(JsonNode rootNode, String cityName) {
        JsonNode currentWeatherNode = rootNode.path("weather").get(0);
        Weather existingWeather = weatherDAO.findWeatherByCityName(cityName);

        if (existingWeather == null) {
            existingWeather = new Weather();
            existingWeather.setCityName(cityName);
        }

        existingWeather.setWeatherId(currentWeatherNode.path("id").asInt());
        existingWeather.setWeather(currentWeatherNode.path("main").asText());
        existingWeather.setDescription(currentWeatherNode.path("description").asText());

        return weatherDAO.save(existingWeather);
    }
}
