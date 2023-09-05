package com.mycompany.SkySong.weather.service.impl;

import com.mycompany.SkySong.weather.client.WeatherApiClient;
import com.mycompany.SkySong.weather.constants.WeatherConstants;
import com.mycompany.SkySong.location.entity.Location;
import com.mycompany.SkySong.weather.entity.Weather;
import com.mycompany.SkySong.weather.entity.WeatherInfo;
import com.mycompany.SkySong.weather.entity.WeatherRequest;
import com.mycompany.SkySong.exception.ValidationException;
import com.mycompany.SkySong.exception.WeatherException;
import com.mycompany.SkySong.location.repository.LocationDAO;
import com.mycompany.SkySong.weather.repository.WeatherDAO;
import com.mycompany.SkySong.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private final LocationDAO locationDAO;
    private final WeatherDAO weatherDAO;
    private final WeatherApiClient weatherApiClient;
    @Autowired
    public WeatherServiceImpl(LocationDAO locationDAO, WeatherDAO weatherDAO, WeatherApiClient weatherApiClient) {
        this.locationDAO = locationDAO;
        this.weatherDAO = weatherDAO;
        this.weatherApiClient = weatherApiClient;
    }

    @Override
    public WeatherInfo getCurrentWeatherByLocationName(String locationName) {
        validateLocationName(locationName);

        try {
            Location location = findLocation(locationName);

            WeatherInfo weatherInfo = fetchWeatherInfo(location);

            createOrUpdateWeatherInDatabase(locationName, location, weatherInfo);

            return weatherInfo;

        } catch (IOException e) {
            log.error("Error fetching weather for location: {} ", locationName, e);
            throw new WeatherException("Error fetching weather for location: " + locationName, e);
        } catch (Exception e) {
            log.error("First you need to specify your location.");
            throw new WeatherException("First you need specify your location.");
        }
    }

    private Location findLocation(String locationName) {
        return locationDAO.findLocationByLocationName(locationName);
    }

    private WeatherInfo fetchWeatherInfo(Location location) throws IOException {
        WeatherRequest weatherRequest = weatherApiClient.fetchWeatherData(location.getLatitude(),
                location.getLongitude());
         return Optional.ofNullable(weatherRequest.weather().get(0))
                .orElseThrow(() -> new WeatherException("No weather information available."));
    }
    private void createOrUpdateWeatherInDatabase(String locationName, Location location, WeatherInfo weatherInfo) {

        Weather existingWeather = weatherDAO.findWeatherByLocationName(locationName);

        if (existingWeather == null) {
           saveNewWeather(location, weatherInfo);
        } else {
            existingWeather.setWeather(weatherInfo.weather());
            existingWeather.setDescription(weatherInfo.description());
        }
    }

    private void saveNewWeather(Location location, WeatherInfo weatherInfo) {
        Weather weather = new Weather();
        weather.setLocationName(location.getLocationName());
        weather.setWeather(weatherInfo.weather());
        weather.setDescription(weatherInfo.description());
        weather.setLocation(location);
        location.setWeather(weather);
        weatherDAO.save(weather);
    }
    @Scheduled(fixedRate = 15 * 60 * 1000)
    private void updateAllWeatherData() throws IOException {
        List<Location> allLocations =  locationDAO.findAll();
        for (Location location : allLocations) {
            updateWeatherForLocation(location);
        }
    }

    private void updateWeatherForLocation(Location location) {
        int retryCount = WeatherConstants.MAX_RETRIES;

        while (retryCount > 0) {
            try {
                getCurrentWeatherByLocationName(location.getLocationName());
                break;
            } catch (WeatherException e) {
                handleRetry(retryCount, location, e);
                retryCount--;

            }
        }
    }
    private void handleRetry(int retryCount, Location location, Exception e) {
        if (retryCount <= 0) {
            log.error("Error updating weather for location {} after multiple retried.",
                    location.getLocationName(), e);
        } else {
            log.info("Retrying update for location {}. Retries left: {}", location.getLocationName(),
                    retryCount);
            sleepBeforeRetry();
        }
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(WeatherConstants.RETRY_SLEEP_MILLIS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("Interrupted during sleep between retries", ex);
        }
    }

    private void validateLocationName(String locationName) {
        Optional.ofNullable(locationName)
                .filter(location -> !location.trim().isEmpty())
                .orElseThrow(() -> new ValidationException(
                        "Location name cannot be null. First you need to specify your location."));
    }
}
