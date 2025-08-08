package com.mycompany.SkySong.infrastructure.spotify.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spotify")
public record SpotifyIntegrationProperties(
        @NotBlank String clientId,
        @NotBlank String clientSecret,
        @NotBlank String scope,
        @NotBlank String redirectUri,
        @NotBlank String authUri,
        @NotBlank String apiTokenBaseUrl
) {}
