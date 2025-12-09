package com.mycompany.skysong.app.config.identity.properties;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "identity.authentication.access-token")
public record AccessTokenProperties(
        @DurationMin(seconds = 1)
        @NotNull Duration ttl) {
}
