package com.mycompany.SkySong.infrastructure.config.webclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient geocodingWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.geoapify.com/v1/geocode/search")
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }
}
