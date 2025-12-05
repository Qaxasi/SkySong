package com.mycompany.SkySong.identity.config;

import jakarta.validation.Valid;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "identity.authentication.cookies")
public record AuthCookieProperties(
        @Valid
        CookieProperties refreshToken,
        @Valid
        CookieProperties userTag
) {
}
