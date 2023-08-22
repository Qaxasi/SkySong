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
import java.util.Optional;

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
        return findLocationInDbOrFetchFromApi(locationName)
                .map(location -> modelMapper.map(location, LocationDto.class))
                .orElseThrow(() -> new GeocodingException("Unable to fetch or map location: " + locationName));
    }

    private Optional<Location> findLocationInDbOrFetchFromApi(String locationName) {
        Location location = locationDAO.findByLocationName(locationName);
        if (location == null) {
            location = fetchLocationFromApiAndSave(locationName);
        }
        return Optional.of(location);
    }

    private Location fetchLocationFromApiAndSave(String locationName) {
        try {
            JsonNode jsonResponse = locationApiClient.fetchGeocodingData(locationName);
            Location location = parseLocationFromJSON(jsonResponse, locationName);
            return locationDAO.save(location);
        } catch (IOException e) {
            throw new GeocodingException("Failed to fetch geocoding data for location: " + locationName, e);
        }
    }


    private void validateLocationName(String locationName) {
        Optional.ofNullable(locationName)
                .filter(location -> !location.trim().isEmpty())
                .orElseThrow(() -> new ValidationException(
                        "Location name cannot be null or empty. First you need to specify your location."));

    }

    private Location parseLocationFromJSON(JsonNode json, String locationName) throws JSONException {

        if (!json.has("data")) {
            throw new JSONException(
                    "The data field is missing in the API reponse.");
        }

        JsonNode dataArray = json.get("data");
        if (dataArray.isArray() && dataArray.size() > 0) {
            JsonNode location = dataArray.get(0);

            if (!location.has("latitude") || !location.has("longitude") ||
                    !location.has("country") || !location.has("region")) {
                throw new JSONException(
                        "Required fields (latitude, longitude, country, region) are missing in the API response");
            }


            Location newLocation = new Location();
            newLocation.setLocationName(locationName);
            newLocation.setLatitude(location.get("latitude").asDouble());
            newLocation.setLongitude(location.get("longitude").asDouble());
            newLocation.setCountry(location.get("country").asText());
            newLocation.setState(location.get("region").asText());

            return newLocation;
        } else {
            throw new JSONException("The 'data' array in the API response is empty or not an array.");
        }
    }
}
