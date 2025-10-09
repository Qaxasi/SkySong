package com.mycompany.SkySong.identity.infrastructure.authentication.token;

import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "identity.authentication.refresh-token")
public record RefreshTokenProperties(
        @NotNull
        @DurationMin(seconds = 1)
        Duration duration,
        @Valid
        CookieProperties cookie) {
}
