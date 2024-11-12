package com.mycompany.SkySong.infrastructure.config.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {

    private HttpClient createHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn
                        .addHandlerFirst(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS)));
    }

    @Bean
    public WebClient spotifyWebClient() {
        return WebClient.builder()
                .baseUrl("https://accounts.spotify.com")
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @Bean
    public WebClient geocodingWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.geoapify.com/v1/geocode/search")
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @Bean
    public WebClient weatherWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/weather?")
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()))
                .defaultHeaders(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }
}
