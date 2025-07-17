package com.mycompany.SkySong.config.jwt;

import com.mycompany.SkySong.config.cookie.CookieProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "application.security.jwt.access-token")
public record JwtAccessTokenProperties(
        @Min(1) int expiration,
        @Valid CookieProperties cookie) {
}
