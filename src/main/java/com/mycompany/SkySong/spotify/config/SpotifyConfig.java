package com.mycompany.SkySong.spotify.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SpotifyAccessTokenCookieProperties.class,
        SpotifyIntegrationProperties.class,
        SpotifyRefreshTokenProperties.class})
public class SpotifyConfig {
}
