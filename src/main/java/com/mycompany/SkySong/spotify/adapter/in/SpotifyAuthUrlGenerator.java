package com.mycompany.SkySong.adapter.music.spotify.authentication.in.access;

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
        return UriComponentsBuilder.fromUriString(properties.getAuthUri())
                .queryParam("client_id", properties.getClientId())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", properties.getRedirectUri())
                .queryParam("scope", properties.getScope())
                .build()
                .toUriString();
    }
}
