package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.client.LocationApiClient;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.entity.LocationRequest;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationDAO locationDAO;
    private final LocationApiClient locationApiClient;

    @Autowired
    public LocationServiceImpl(LocationDAO locationDAO, LocationApiClient locationApiClient) {
        this.locationDAO = locationDAO;
        this.locationApiClient = locationApiClient;
    }

    @Override
    @Transactional
    public LocationRequest fetchAndSaveCoordinates(String locationName) throws IOException {
        validateLocationName(locationName);

        try {
            LocationRequest locationRequest = locationApiClient.fetchGeocodingData(locationName);

            validateLocationRequest(locationRequest);

            saveLocationIfNotExist(locationName, locationRequest);

            return locationRequest;
        } catch (IOException e) {
            log.error("Failed to fetch geocoding data", e);
            throw new ServiceException("Failed to fetch geocoding data", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred");
            throw new ServiceException("An unexpected error occurred", e);
        }
    }

    private void validateLocationRequest(LocationRequest locationRequest) {
        if (locationRequest == null) {
            throw new ServiceException("Received null LocationRequest from API");
        }
    }

    private void validateLocationName(String locationName) {
        if (locationName == null || locationName.isEmpty()) {
            throw new ServiceException("Location name cannot be null or empty");
        }
    }

    private void saveLocationIfNotExist(String locationName, LocationRequest locationRequest) {
        Location existingLocation = locationDAO.findLocationByLocationName(locationName);
        if (existingLocation == null) {
            saveLocation(locationRequest);
        }
    }

    private void saveLocation(LocationRequest locationRequest) {
        Location location = mapToLocationEntity(locationRequest);
        locationDAO.save(location);
        log.info("Successfully saved the location {}", location.getLocationName());
    }
    private Location mapToLocationEntity(LocationRequest locationRequest) {
        Location location = new Location();
        location.setLocationName(locationRequest.locationName());
        location.setLatitude(locationRequest.latitude());
        location.setLongitude(locationRequest.longitude());
        location.setState(locationRequest.state());
        location.setCountry(locationRequest.country());
        return location;
    }


}
