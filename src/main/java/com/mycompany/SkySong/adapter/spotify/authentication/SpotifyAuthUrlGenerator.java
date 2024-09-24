package com.mycompany.SkySong.adapter.spotify.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SpotifyAuthUrlGenerator {

    private final String authUri;
    private final String spotifyClientId;
    private final String redirectUri;
    private final String scope;

    public SpotifyAuthUrlGenerator(@Value("${SPOTIFY_AUTH_URI}") String authUri,
                                   @Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                                   @Value("${REDIRECT_URI}") String redirectUri,
                                   @Value("${SPOTIFY_SCOPE}") String scope) {
        this.authUri = authUri;
        this.spotifyClientId = spotifyClientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    public String getAuthorizationCodeUrl() {
        return UriComponentsBuilder.fromUriString(authUri)
                .queryParam("client_id", spotifyClientId)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", URLEncoder.encode(redirectUri, StandardCharsets.UTF_8))
                .queryParam("scope", scope)
                .build()
                .toUriString();
    }
}
