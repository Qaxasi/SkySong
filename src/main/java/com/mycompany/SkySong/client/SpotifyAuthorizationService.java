package com.mycompany.SkySong.client;

import com.mycompany.SkySong.entity.SpotifyAccessToken;
import com.mycompany.SkySong.entity.TokenRequest;
import com.mycompany.SkySong.exception.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class SpotifyAuthorizationService {
    private static final String AUTH_URI = "https://accounts.spotify.com/login";
    private static final String GRANT_TYPE = "authorization_code";
    private final String spotifyClientId;
    private final String spotifyClientSecret;
    private final String redirect_uri;
    private final WebClient webClient;
    @Autowired
    public SpotifyAuthorizationService(@Value("${SPOTIFY_CLIENT_ID}") String spotifyClientId,
                                       @Value("${SPOTIFY_CLIENT_SECRET}") String spotifyClientSecret,
                                       @Qualifier("account") WebClient webClient,
                                       @Value("${REDIRECT_URI}") String redirect_uri) {
        this.spotifyClientId = spotifyClientId;
        this.spotifyClientSecret = spotifyClientSecret;
        this.webClient = webClient;
        this.redirect_uri = redirect_uri;
    }

    public SpotifyAccessToken getAccessToken(String authorizationCode) throws AuthorizationException {
        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/").build())
                    .headers(httpHeaders -> httpHeaders.setBasicAuth(setEncodedAuthorizationCredentials()))
                    .bodyValue(new TokenRequest(GRANT_TYPE, authorizationCode, redirect_uri).toMultiValueMap())
                    .retrieve()
                    .bodyToMono(SpotifyAccessToken.class)
                    .block();
        } catch (Exception e) {
            throw new AuthorizationException("Failed to get access token from Spotify", e);
        }
    }

    public String getAuthorizationCodeURL() {
        return setAuthorizationCodeURL();
    }

    private String setEncodedAuthorizationCredentials() {
        String credentials = spotifyClientId + ":" + spotifyClientSecret;
        return new String(Base64.getEncoder().encode(credentials.getBytes()));
    }
    private String setAuthorizationCodeURL()  {
        return AUTH_URI + "?" + tryBuildingAuthorizationParams();
    }

    private String tryBuildingAuthorizationParams() {
        try {
            return buildAuthorizationParams();
        } catch (UnsupportedEncodingException e) {
            log.error("Exception occurred during request building process.", e);
            throw new AuthorizationException("Exception occurred during request building process.");
        }
    }
    private String buildAuthorizationParams() throws UnsupportedEncodingException {
        return "client_id=" + spotifyClientId
                + "&response_type=code"
                + "&redirect_uri=" + URLEncoder.encode(redirect_uri, UTF_8)
                + "&scope=playlist-modify-public";
    }
}
