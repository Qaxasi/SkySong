package com.mycompany.SkySong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class ApplicationConfiguration {
    private static final String SPOTIFY_API_BASE_URI = "https://api.spotify.com/v1/";
    private static final String SPOTIFY_TOKEN_URI = "https://accounts.spotify.com/api/token";
    private static final String GEOCODING_API_URL_TEMPLATE = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String WEATHER_API_URL_TEMPLATE = "https://api.openweathermap.org/data/2.5/weather";

    @Bean
    @Primary
    public WebClient spotifyApiWebClient() {
        return WebClient.builder()
                .baseUrl(SPOTIFY_API_BASE_URI)
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @Bean("account")
    public WebClient accountWebClient() {
        return WebClient.builder()
                .baseUrl(SPOTIFY_TOKEN_URI)
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .build();
    }

    @Bean("location")
    public WebClient locationWebClient() {
        return WebClient.builder()
                .baseUrl(GEOCODING_API_URL_TEMPLATE)
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }
    @Bean("weather")
    public WebClient weatherWebClient() {
        return WebClient.builder()
                .baseUrl(WEATHER_API_URL_TEMPLATE)
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }
}
