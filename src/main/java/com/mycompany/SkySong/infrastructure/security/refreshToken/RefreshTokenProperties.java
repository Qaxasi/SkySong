package com.mycompany.SkySong.config.refreshToken;

import com.mycompany.SkySong.config.cookie.CookieProperties;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "application.security.refresh-token")
public record RefreshTokenProperties(
        @NotNull Duration duration,
        @Valid CookieProperties cookie) {
}
