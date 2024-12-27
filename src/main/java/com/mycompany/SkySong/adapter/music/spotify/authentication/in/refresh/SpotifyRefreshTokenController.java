package com.mycompany.SkySong.adapter.music.spotify.authentication.in.refresh;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.refresh.SpotifyTokenRefresher;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.infrastructure.context.UserContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyRefreshTokenController {

    private final SpotifyTokenRefresher refresher;
    private final CookieUtils cookieUtils;

    public SpotifyRefreshTokenController(SpotifyTokenRefresher refresher,
                                         CookieUtils cookieUtils) {
        this.refresher = refresher;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshAccessToken() {
        Integer userId = UserContext.getUserId();

        String newAccessToken = refresher.refreshAccessToken(userId);

        ResponseCookie cookie = cookieUtils.generateCookie("spotifyAccessToken", newAccessToken, "/api/v1/spotify/", 3600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Access token refreshed successfully."));
    }
}
