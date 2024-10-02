package com.mycompany.SkySong.adapter.spotify.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.SpotifyRefreshTokenHandler;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyRefreshTokenController {

    private final SpotifyRefreshTokenHandler refreshTokenHandler;

    public SpotifyRefreshTokenController(SpotifyRefreshTokenHandler refreshTokenHandler) {
        this.refreshTokenHandler = refreshTokenHandler;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(@CookieValue(name = "jwtToken") String jwtToken,
                                                     HttpServletResponse response) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        SpotifyAccessTokenResponse newAccessToken = refreshTokenHandler.refreshSpotifyAccessToken(jwtToken);

        Cookie accessTokenCookie = new Cookie("spotifyAccessToken", newAccessToken.accessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/api/v1/spotify/");
        accessTokenCookie.setMaxAge(3600);
        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok("Access token refreshed successfully.");
    }
}
