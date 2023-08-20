package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.WeatherApiClient;
import com.mycompany.SkySong.dto.LocationDto;
import com.mycompany.SkySong.dto.WeatherDto;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.entity.Weather;

import com.mycompany.SkySong.exception.*;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.repository.WeatherDAO;
import com.mycompany.SkySong.service.WeatherService;

import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    public WeatherServiceImpl(WeatherDAO weatherDAO, LocationDAO locationDAO, WeatherApiClient weatherApiClient,
                              ModelMapper modelMapper) {
        this.weatherDAO = weatherDAO;
        this.locationDAO = locationDAO;
        this.weatherApiClient = weatherApiClient;
        this.modelMapper = modelMapper;
    }
    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Override
    public WeatherDto getCurrentWeatherByLocationName(String locationName) throws IOException {
        validateLocationName(locationName);

        try {
            Location location = findLocationByName(locationName);
            JsonNode rootNode = weatherApiClient.fetchWeatherData(location.getLatitude(), location.getLongitude());
            return saveOrUpdateWeatherData(rootNode, location);
        } catch (Exception e) {
            throw new WeatherException("Error fetching weather for location: " + locationName, e);
        }
    }

    private void validateLocationName(String locationName) {
        Optional.ofNullable(locationName)
                .filter(location -> !location.trim().isEmpty())
                .orElseThrow(() -> new ValidationException(
                        "Location name cannot be null. First you need to specify your location."));
    }
    private Location findLocationByName(String locationName) {
        return Optional.ofNullable(locationDAO.findByLocationName(locationName))
                .orElseThrow(() -> new LocationNotFoundException("Geocoding not found for location: " + locationName));
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    private void updateAllWeatherData() throws IOException {
        List<Location> allEntities = locationDAO.findAll();
        List<LocationDto> allLocations = allEntities.stream()
                .map(entity -> modelMapper.map(entity, LocationDto.class))
                .toList();
        for (LocationDto location : allLocations) {
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

    private WeatherDto saveOrUpdateWeatherData(JsonNode rootNode, Location location) {
        try {
            Weather existingWeather = Optional.ofNullable(weatherDAO.findWeatherByLocationName(
                    location.getLocationName())).orElseGet(() -> {
                Weather weather = new Weather();
                weather.setLocationName(location.getLocationName());
                return weather;
            });

            mapJsonToWeather(rootNode, existingWeather);

            existingWeather.setLocation(location);

            location.setWeather(existingWeather);

            Weather savedWeather = weatherDAO.save(existingWeather);
            return modelMapper.map(savedWeather, WeatherDto.class);
        } catch (Exception e) {
            throw new WeatherDataSaveException("Error saving or updating weather data for location: " + location.getLocationName(), e);
        }
    }

    private void mapJsonToWeather(JsonNode rootNode, Weather existingWeather) {
        JsonNode currentWeather = rootNode.path("weather").get(0);
        existingWeather.setWeatherId(currentWeather.path("id").asInt());
        existingWeather.setWeather(currentWeather.path("main").asText());
        existingWeather.setDescription(currentWeather.path("description").asText());
    }
}
