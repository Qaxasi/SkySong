package com.mycompany.SkySong.config;

import com.mycompany.SkySong.config.jwt.JwtAccessTokenProperties;
import com.mycompany.SkySong.config.jwt.JwtCoreProperties;
import com.mycompany.SkySong.config.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.config.spotify.SpotifyAccessTokenCookieProperties;
import com.mycompany.SkySong.config.spotify.SpotifyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SpotifyAccessTokenCookieProperties.class,
        SpotifyProperties.class,
        JwtCoreProperties.class,
        JwtAccessTokenProperties.class,
        RefreshTokenProperties.class})
public class AppPropertiesConfig {
}
