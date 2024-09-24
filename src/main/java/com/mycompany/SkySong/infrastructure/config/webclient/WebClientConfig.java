package com.mycompany.SkySong.infrastructure.config.webclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient spotifyWebClient() {
        return WebClient.builder()
                .baseUrl("https://accounts.spotify.com")
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @Bean
    public WebClient geocodingWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.geoapify.com/v1/geocode/search")
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @Bean
    public WebClient weatherWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/weather?")
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }
}
