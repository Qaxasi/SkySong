package com.mycompany.SkySong.adapter.spotify.authentication.token.access.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler.SpotifyAccessTokenHandler;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/auth")
public class SpotifyCallbackController {

    private final SpotifyAccessTokenHandler tokenHandler;
    private final CookieUtils cookieUtils;

    public SpotifyCallbackController(SpotifyAccessTokenHandler tokenHandler,
                                     CookieUtils cookieUtils) {
        this.tokenHandler = tokenHandler;
        this.cookieUtils = cookieUtils;
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

        ResponseCookie cookie = cookieUtils.generateCookie("spotifyAccessToken", accessToken, "/api/v1/spotify", 3600);


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Spotify authorization successful."));
    }
}
