package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.client.LocationApiClient;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.entity.LocationRequest;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.service.LocationService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
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
        try {
            LocationRequest locationRequest = locationApiClient.fetchGeocodingData(locationName);
            ensureLocationIsSaved(locationName, locationRequest);
            return locationRequest;
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch geocoding data", e);
        } catch (Exception e) {
            throw new ServiceException("An unexpected error occurred", e);
        }
    }

    private void ensureLocationIsSaved(String locationName, LocationRequest locationRequest) {
        Location existingLocation = locationDAO.findLocationByLocationName(locationName);
        if (existingLocation == null) {
            saveLocation(locationRequest);
        }
    }

    private void saveLocation(LocationRequest locationRequest) {
        Location location = mapToLocationEntity(locationRequest);
        locationDAO.save(location);
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
