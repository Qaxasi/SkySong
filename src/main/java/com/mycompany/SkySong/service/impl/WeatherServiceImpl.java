package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.WeatherApiClient;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.entity.Weather;

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
    public Weather getCurrentWeatherByLocalityName(String localityName) throws IOException {
        Location location = findLocationByLocalityName(localityName);
        JsonNode rootNode = weatherApiClient.fetchWeatherData(location.getLatitude(), location.getLongitude());
        return saveOrUpdateWeatherData(rootNode, localityName);

    }

    private Location findLocationByLocalityName(String localityName) {
        return locationDAO.findByLocalityName(localityName);
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    private void updateAllWeatherData() throws IOException {
        List<Location> allLocations = locationDAO.findAll();
        for (Location location : allLocations) {
            getCurrentWeatherByLocalityName(location.getLocalityName());
        }
    }

    private Weather saveOrUpdateWeatherData(JsonNode rootNode, String localityName) {
        JsonNode currentWeatherNode = rootNode.path("weather").get(0);
        Weather existingWeather = weatherDAO.findWeatherByLocalityName(localityName);

        if (existingWeather == null) {
            existingWeather = new Weather();
            existingWeather.setLocalityName(localityName);
        }

        existingWeather.setWeatherId(currentWeatherNode.path("id").asInt());
        existingWeather.setWeather(currentWeatherNode.path("main").asText());
        existingWeather.setDescription(currentWeatherNode.path("description").asText());

        return weatherDAO.save(existingWeather);
    }
}
