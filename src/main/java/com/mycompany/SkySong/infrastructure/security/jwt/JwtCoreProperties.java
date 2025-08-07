package com.mycompany.SkySong.config.jwt;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application.security.jwt")
public record JwtCoreProperties(
        @NotBlank String secretKey) {
}
