package com.mycompany.SkySong.infrastructure.spotify.config;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties("spotify.refresh-token")
public class SpotifyRefreshTokenProperties {
    @Min(1) int ttlDays;

    public Duration ttlAsDuration() {
        return Duration.ofDays(ttlDays);
    }
}
