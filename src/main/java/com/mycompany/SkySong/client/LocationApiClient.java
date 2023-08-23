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
            "http://api.positionstack.com/v1/forward?access_key=%s&query=%s";
    private final String API_KEY = System.getenv("GEOCODING_API_KEY");
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchGeocodingData(String locationName) throws IOException {
        if (locationName == null || locationName.trim().isEmpty()) {
            throw new ValidationException(
                    "Location name cannot be null or empty. First you need to specify your location.");
        }

        String apiUrl = String.format(GEOCODING_API_URL_TEMPLATE, API_KEY, locationName);
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

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
