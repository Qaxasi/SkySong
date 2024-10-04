package com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler;

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
        if (authUri == null || authUri.isEmpty()) {
             throw new IllegalArgumentException("Authorization uri is missing or invalid");
        }
        if (spotifyClientId == null || spotifyClientId.isEmpty()) {
            throw new IllegalArgumentException("Spotify client id is missing or invalid");
        }
        if (redirectUri == null || redirectUri.isEmpty()) {
            throw new IllegalArgumentException("Redirect Uri is missing or invalid");
        }
        if (scope == null || scope.isEmpty()) {
            throw new IllegalArgumentException("Scope is missing or invalid");
        }

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
