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
    public Weather getCurrentWeatherByLocalityName(String localityName) throws IOException {
        validateLocalityName(localityName);

        try {
            Location location = findLocationByLocalityName(localityName);
            JsonNode rootNode = weatherApiClient.fetchWeatherData(location.getLatitude(), location.getLongitude());
            return saveOrUpdateWeatherData(rootNode, localityName);
        } catch (Exception e) {
            throw new WeatherException("Error fetching weather for locality: " + localityName, e);
        }
    }

    private void validateLocalityName(String localityName) {
        if (localityName == null || localityName.trim().isEmpty()) {
            throw new ValidationException("Locality name cannot be null. First you need to specify your location");
        }
    }
    private Location findLocationByLocalityName(String localityName) {
        try {
            return locationDAO.findByLocalityName(localityName);
        } catch (Exception e) {
            throw new LocationNotFoundException("Geocoding not found for locality: " + localityName, e);
        }

    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    private void updateAllWeatherData() throws IOException {
        List<Location> allLocations = locationDAO.findAll();
        for (Location location : allLocations) {
            int retryCount = 3;
            while (retryCount > 0) {
                try {
                    getCurrentWeatherByLocalityName(location.getLocalityName());
                    break;
                } catch (WeatherException e) {
                    retryCount--;

                    if (retryCount <= 0) {
                        logger.error("Error updating weather for locality after multiple retried: {}",
                                location.getLocalityName(), e);
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
                    logger.error("Location not found for locality: {}", location.getLocalityName(), e);
                }
            }
        }
    }

    private Weather saveOrUpdateWeatherData(JsonNode rootNode, String localityName) {
        try {
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
        } catch (Exception e) {
            throw new WeatherDataSaveException(
                    "Error saving or updating weather data for locality: " + localityName, e);
        }
    }
}
