package com.mycompany.SkySong.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherApiClient {

    private static final String WEATHER_API_URL_TEMPLATE =
            "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s";
    private final String API_KEY = System.getenv("WEATHER_API_KEY");
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchWeatherData(double latitude, double longitude) throws IOException {
        String apiUrl = String.format(WEATHER_API_URL_TEMPLATE, latitude, longitude, API_KEY);
        Request request = new Request.Builder().url(apiUrl).build();

        try(Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return objectMapper.readTree(response.body().string());
            }
        }
        throw new IOException("Failed to fetch weather data.");
    }
}
