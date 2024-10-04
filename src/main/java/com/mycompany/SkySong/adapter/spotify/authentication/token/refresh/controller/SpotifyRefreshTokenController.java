package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.handler.SpotifyRefreshTokenHandler;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyRefreshTokenController {

    private final SpotifyRefreshTokenHandler refreshTokenHandler;
    private final CookieUtils cookieUtils;

    public SpotifyRefreshTokenController(SpotifyRefreshTokenHandler refreshTokenHandler,
                                         CookieUtils cookieUtils) {
        this.refreshTokenHandler = refreshTokenHandler;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshAccessToken(@CookieValue(name = "jwtToken") String jwtToken,
                                                          HttpServletResponse response) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new IllegalArgumentException("Jwt token is missing or invalid");
        }

        String newAccessToken = refreshTokenHandler.refreshSpotifyAccessToken(jwtToken);

        ResponseCookie cookie = cookieUtils.generateCookie("spotifyAccessToken", newAccessToken, "/api/v1/spotify/", 3600);



        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Access token refreshed successfully."));
    }
}
