package com.mycompany.SkySong.shared;

import com.mycompany.SkySong.infrastructure.security.jwt.JwtAccessTokenProperties;
import com.mycompany.SkySong.infrastructure.security.jwt.JwtCoreProperties;
import com.mycompany.SkySong.infrastructure.security.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.spotify.config.SpotifyAccessTokenCookieProperties;
import com.mycompany.SkySong.spotify.config.SpotifyIntegrationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SpotifyAccessTokenCookieProperties.class,
        SpotifyIntegrationProperties.class,
        JwtCoreProperties.class,
        JwtAccessTokenProperties.class,
        RefreshTokenProperties.class})
public class AppPropertiesConfig {
}
