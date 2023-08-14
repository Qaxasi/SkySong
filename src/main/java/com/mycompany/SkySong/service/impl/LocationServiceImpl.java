package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.entity.Location;
import com.mycompany.SkySong.repository.LocationDAO;
import com.mycompany.SkySong.service.LocationService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class LocationServiceImpl implements LocationService {
    private LocationDAO locationDAO;

    public LocationServiceImpl(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }
    String API_KEY = System.getenv("WEATHER_API_KEY");
    @Override
    public Location getLocationCoordinatesByCityName(String cityName) {
        Location location = locationDAO.findByCityName(cityName);
        if (location != null) {
            return location;
        }

        try {
            String jsonResponse = getCityDataFromAPI(cityName);
            location = parseLocationFromJSON(jsonResponse, cityName);
            location = locationDAO.save(location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return location;
    }


    private String getCityDataFromAPI(String cityName) throws IOException {
        URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&appid=" + API_KEY);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder outputStringBuilder = new StringBuilder();
        String output;

        while ((output = br.readLine()) != null) {
            outputStringBuilder.append(output);
        }
        return outputStringBuilder.toString();
    }

    private Location parseLocationFromJSON(String json, String cityName) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        JSONObject city = jsonArray.getJSONObject(0);
        double lat = city.getDouble("lat");
        double lon = city.getDouble("lon");

        Location location = new Location();
        location.setCityName(cityName);
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }
}
