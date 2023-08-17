package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.WeatherApiClient;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.entity.Weather;

import com.mycompany.SkySong.exception.*;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.repository.WeatherDAO;
import com.mycompany.SkySong.service.WeatherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Override
    public Weather getCurrentWeatherByLocationName(String locationName) throws IOException {
        validateLocationName(locationName);

        try {
            Location location = findLocationByName(locationName);
            JsonNode rootNode = weatherApiClient.fetchWeatherData(location.getLatitude(), location.getLongitude());
            return saveOrUpdateWeatherData(rootNode, locationName);
        } catch (Exception e) {
            throw new WeatherException("Error fetching weather for location: " + locationName, e);
        }
    }

    private void validateLocationName(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            throw new ValidationException("Location name cannot be null. First you need to specify your location.");
        }
    }
    private Location findLocationByName(String locationName) {
        try {
            return locationDAO.findByLocationName(locationName);
        } catch (Exception e) {
            throw new LocationNotFoundException("Geocoding not found for location: " + locationName, e);
        }

    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    private void updateAllWeatherData() throws IOException {
        List<Location> allLocations = locationDAO.findAll();
        for (Location location : allLocations) {
            int retryCount = 3;
            while (retryCount > 0) {
                try {
                    getCurrentWeatherByLocationName(location.getLocationName());
                    break;
                } catch (WeatherException e) {
                    retryCount--;

                    if (retryCount <= 0) {
                        logger.error("Error updating weather for location after multiple retried: {}",
                                location.getLocationName(), e);
                    } else {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();

                            logger.error("Interrupted during sleep between retries", ex);
                            break;
                        }
                    }
                } catch (LocationNotFoundException e) {
                    logger.error("Location not found for locality: {}", location.getLocationName(), e);
                }
            }
        }
    }

    private Weather saveOrUpdateWeatherData(JsonNode rootNode, String locationName) {
        try {
            JsonNode currentWeatherNode = rootNode.path("weather").get(0);
            Weather existingWeather = weatherDAO.findWeatherByLocationName(locationName);

            if (existingWeather == null) {
                existingWeather = new Weather();
                existingWeather.setLocationName(locationName);
            }

            existingWeather.setWeatherId(currentWeatherNode.path("id").asInt());
            existingWeather.setWeather(currentWeatherNode.path("main").asText());
            existingWeather.setDescription(currentWeatherNode.path("description").asText());

            return weatherDAO.save(existingWeather);
        } catch (Exception e) {
            throw new WeatherDataSaveException(
                    "Error saving or updating weather data for location: " + locationName, e);
        }
    }
}
