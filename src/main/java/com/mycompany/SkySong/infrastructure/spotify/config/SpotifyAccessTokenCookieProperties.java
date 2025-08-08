package com.mycompany.SkySong.infrastructure.spotify.config;

import com.mycompany.SkySong.shared.cookie.CookieProperties;
import jakarta.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spotify.access-token")
public record SpotifyAccessTokenCookieProperties(
        @Valid CookieProperties cookie) {
}
