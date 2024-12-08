package com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.token.refresh.handler.SpotifyRefreshTokenHandler;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mycompany.SkySong.shared.utils.ErrorStatusMapper.mapErrorTypeToStatus;

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
    public ResponseEntity<ApiResponse> refreshAccessToken(@CookieValue(name = "jwtToken") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Jwt token is empty"));
        }

        Result<String> newAccessTokenResult = refreshTokenHandler.refreshSpotifyAccessToken(jwtToken);
        if (!newAccessTokenResult.success()) {
            return ResponseEntity.status(mapErrorTypeToStatus(newAccessTokenResult.errorType()))
                    .body(new ApiResponse(""));
        }

        String newAccessToken = newAccessTokenResult.data();
        ResponseCookie cookie = cookieUtils.generateCookie("spotifyAccessToken", newAccessToken, "/api/v1/spotify/", 3600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Access token refreshed successfully."));
    }
}
