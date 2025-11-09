package com.mycompany.SkySong.identity.infrastructure.authentication.config.token;

import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "identity.authentication.session")
public record SessionProperties(
        @NotNull
        @DurationMin(seconds = 1)
        Duration ttl,
        @Valid
        CookieProperties cookie) {
}
