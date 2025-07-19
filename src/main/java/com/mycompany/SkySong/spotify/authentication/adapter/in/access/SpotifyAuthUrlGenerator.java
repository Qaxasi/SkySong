package com.mycompany.SkySong.spotify.adapter.authentication.in.access;

import com.mycompany.SkySong.config.spotify.SpotifyProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SpotifyAuthUrlGenerator {
    private final SpotifyProperties properties;

    public SpotifyAuthUrlGenerator(final SpotifyProperties properties) {
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
