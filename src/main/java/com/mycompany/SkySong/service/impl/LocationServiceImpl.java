package com.mycompany.SkySong.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.mycompany.SkySong.client.LocationApiClient;
import com.mycompany.SkySong.entity.Location;
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
    public Location getLocationCoordinatesByCityName(String cityName) {
        Location location = locationDAO.findByCityName(cityName);
        if (location != null) {
            return location;
        }

        try {
            JsonNode jsonResponse = locationApiClient.fetchGeocodingData(cityName);
            location = parseLocationFromJSON(jsonResponse, cityName);
            location = locationDAO.save(location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return location;
    }

    private Location parseLocationFromJSON(JsonNode json, String cityName) throws JSONException {
        JsonNode city = json.get(0);

        double lat = city.get("lat").asDouble();
        double lon = city.get("lon").asDouble();

        Location location = new Location();
        location.setCityName(cityName);
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }
}
