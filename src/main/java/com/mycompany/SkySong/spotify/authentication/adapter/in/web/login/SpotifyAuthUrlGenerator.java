package com.mycompany.SkySong.spotify.authentication.adapter.in.web.login;

import com.mycompany.SkySong.spotify.config.SpotifyIntegrationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SpotifyAuthUrlGenerator {
    private final SpotifyIntegrationProperties properties;

    public SpotifyAuthUrlGenerator(final SpotifyIntegrationProperties properties) {
        this.properties = properties;
    }

    public String getAuthorizationCodeUrl() {
        return UriComponentsBuilder.fromUriString(properties.authUri())
                .queryParam("client_id", properties.clientId())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("scope", properties.scope())
                .build()
                .toUriString();
    }
}
