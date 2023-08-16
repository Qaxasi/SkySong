package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.LocationApiClient;
import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.exception.GeocodingException;
import com.mycompany.SkySong.exception.ValidationException;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.service.LocationService;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class LocationServiceImpl implements LocationService {
    private LocationDAO locationDAO;
    private LocationApiClient locationApiClient;

    public LocationServiceImpl(LocationDAO locationDAO, LocationApiClient locationApiClient) {
        this.locationDAO = locationDAO;
        this.locationApiClient = locationApiClient;
    }
    @Override
    public Location getLocationCoordinatesByLocalityName(String localityName) {
        if (localityName == null || localityName.trim().isEmpty()) {
            throw new ValidationException(
                    "City name cannot be null or empty. First you need to specify your location.");
        }
        Location location = locationDAO.findByLocalityName(localityName);
        if (location != null) {
            return location;
        }

        try {
            JsonNode jsonResponse = locationApiClient.fetchGeocodingData(localityName);
            location = parseLocationFromJSON(jsonResponse, localityName);
            location = locationDAO.save(location);
        } catch (IOException e) {
            throw new GeocodingException("Failed to fetch geocoding data for city: " + localityName, e);
        }
        return location;
    }

    private Location parseLocationFromJSON(JsonNode json, String localityName) throws JSONException {
        JsonNode locality = json.get(0);

        double lat = locality.get("lat").asDouble();
        double lon = locality.get("lon").asDouble();

        Location location = new Location();
        location.setLocalityName(localityName);
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }
}
