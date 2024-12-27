package com.mycompany.SkySong.adapter.music.spotify.authentication.in;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.handler.SpotifyRefreshTokenHandler;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.utils.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.mycompany.SkySong.shared.utils.ErrorStatusMapper.mapErrorTypeToStatus;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyRefreshTokenController {

    private final SpotifyRefreshTokenHandler handler;
    private final CookieUtils cookieUtils;

    public SpotifyRefreshTokenController(SpotifyRefreshTokenHandler handler,
                                         CookieUtils cookieUtils) {
        this.handler = handler;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshAccessToken() {
        Result<String> newAccessTokenResult = authentication.refreshUserAccessToken(jwtToken);
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
