package com.mycompany.SkySong.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.SkySong.exception.GeocodingException;
import com.mycompany.SkySong.exception.ValidationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class LocationApiClient {

    private static final String GEOCODING_API_URL_TEMPLATE =
            "http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s";
    private final String API_KEY = System.getenv("WEATHER_API_KEY");
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchGeocodingData(String cityName) throws IOException {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new ValidationException(
                    "City name cannot be null or empty. First you need to specify your location.");
        }

        String apiUrl = String.format(GEOCODING_API_URL_TEMPLATE, cityName, API_KEY);
        Request request = new Request.Builder().url(apiUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.body().string());
                if (jsonNode.has("error")) {
                    throw new GeocodingException("API returned error: " + jsonNode.get("error").asText());
                }
                return jsonNode;
            } else {
                throw new GeocodingException("Failed to fetch geocoding data. Response code: " + response.code());
            }
        } catch (IOException e) {
            throw new GeocodingException("Error communicating with the geocoding API.", e);
        }
    }
}
