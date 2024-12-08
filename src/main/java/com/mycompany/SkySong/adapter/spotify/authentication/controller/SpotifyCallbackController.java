package com.mycompany.SkySong.adapter.spotify.authentication.token.access.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.token.access.handler.SpotifyAccessTokenHandler;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.utils.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mycompany.SkySong.shared.utils.ErrorStatusMapper.mapErrorTypeToStatus;

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
                                                             @CookieValue(name = "jwtToken") String jwtToken) {
        if (jwtToken.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Missing authorization token. Please try logging in again."));
        }

        Result<String> accessTokenResult = tokenHandler.retrieveSpotifyAccessToken(authCode, jwtToken);
        if (!accessTokenResult.success()) {
            return ResponseEntity.status(mapErrorTypeToStatus(accessTokenResult.errorType()))
                    .body(new ApiResponse("We encountered an issue processing your request. Please try again later."));
        }

        String accessToken = accessTokenResult.data();
        ResponseCookie cookie = cookieUtils.generateCookie("spotifyAccessToken", accessToken, "/api/v1/spotify", 3600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Spotify authorization successful."));
    }
}
