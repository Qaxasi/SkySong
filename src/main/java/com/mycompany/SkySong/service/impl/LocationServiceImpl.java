package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.LocationApiClient;

import com.mycompany.SkySong.dto.LocationDto;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.exception.GeocodingException;
import com.mycompany.SkySong.exception.ValidationException;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.service.LocationService;
import org.json.JSONException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class LocationServiceImpl implements LocationService {
    private LocationDAO locationDAO;
    private LocationApiClient locationApiClient;
    private ModelMapper modelMapper;

    public LocationServiceImpl(LocationDAO locationDAO, LocationApiClient locationApiClient, ModelMapper modelMapper) {
        this.locationDAO = locationDAO;
        this.locationApiClient = locationApiClient;
        this.modelMapper = modelMapper;
    }
    @Override
    public LocationDto getLocationCoordinatesByLocationName(String locationName) {
        validateLocationName(locationName);
        Location location = locationDAO.findByLocationName(locationName);
        if (location != null) {
            return modelMapper.map(location, LocationDto.class);
        }
        try {
            JsonNode jsonResponse = locationApiClient.fetchGeocodingData(locationName);
            location = parseLocationFromJSON(jsonResponse, locationName);
            location = locationDAO.save(location);
        } catch (IOException e) {
            throw new GeocodingException("Failed to fetch geocoding data for location: " + locationName, e);
        }
        return modelMapper.map(location, LocationDto.class);
    }
    private void validateLocationName(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            throw new ValidationException(
                    "Location name cannot be null or empty. First you need to specify your location.");
        }
    }

    private Location parseLocationFromJSON(JsonNode json, String locationName) throws JSONException {
        if (json == null || !json.isArray() || json.size() == 0) {
            throw new JSONException("Unexpected JSON format received from the geocoding API.");
        }

        JsonNode location = json.get(0);
        if (!location.has("lat") || !location.has("lon") || !location.has("country") ||
            !location.has("state")) {
            throw new JSONException("Required fields (lat, lon, country, state) are missing in the API response.");
        }

        Location newLocation = new Location();
        newLocation.setLocationName(locationName);
        newLocation.setLatitude(location.get("lat").asDouble());
        newLocation.setLongitude(location.get("lon").asDouble());
        newLocation.setCountry(location.get("country").asText());
        newLocation.setState(location.get("state").asText());

        return newLocation;
    }
}
