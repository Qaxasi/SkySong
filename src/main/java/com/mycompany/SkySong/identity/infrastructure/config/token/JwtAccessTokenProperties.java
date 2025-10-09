package com.mycompany.SkySong.identity.infrastructure.config.token;

import com.mycompany.SkySong.shared.cookie.CookieProperties;
import jakarta.validation.Valid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application.security.jwt.access-token")
public record JwtAccessTokenProperties(
        @Min(1) int expiration,
        @Valid CookieProperties cookie) {
}
