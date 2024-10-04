package com.mycompany.SkySong.adapter.spotify.authentication.token.access.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler.SpotifyAccessTokenHandler;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/auth")
public class SpotifyCallbackController {

    private final SpotifyAccessTokenHandler tokenHandler;

    public SpotifyCallbackController(SpotifyAccessTokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse> handleSpotifyCallback(@RequestParam("code") String authCode,
                                                             @CookieValue(name = "jwtToken") String jwtToken,
                                                             HttpServletResponse response) {
        if (authCode == null || authCode.isEmpty()) {
            throw new IllegalArgumentException("Authorization code is missing or invalid");
        }
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        String accessToken = tokenHandler.retrieveSpotifyAccessToken(authCode, jwtToken);

        Cookie accessTokenCookie = new Cookie("spotifyAccessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/api/v1/spotify/");
        accessTokenCookie.setMaxAge(3600);
        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok(new ApiResponse("Spotify authorization successful."));
    }
}
