package com.mycompany.SkySong.spotify.authentication.adapter.in.web.refresh;

import com.mycompany.SkySong.spotify.authentication.application.refresh.SpotifyAccessTokenRefresher;
import com.mycompany.SkySong.spotify.config.SpotifyAccessTokenCookieProperties;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.infrastructure.context.UserContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyAccessTokenRefreshController {
    private final SpotifyAccessTokenRefresher refresher;
    private final CookieUtils cookieUtils;
    private final SpotifyAccessTokenCookieProperties properties;

    public SpotifyAccessTokenRefreshController(final SpotifyAccessTokenRefresher accessTokenRefresher,
                                               final CookieUtils cookieUtils,
                                               final SpotifyAccessTokenCookieProperties properties) {
        this.refresher = accessTokenRefresher;
        this.cookieUtils = cookieUtils;
        this.properties = properties;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshAccessToken() {
        final Integer userId = UserContext.getUserId();
        return refresher.refreshAccessToken(userId)
                .fold(
                        error -> ResponseEntity
                                .status(error.errorType().getHttpStatus())
                                .body(error.toErrorResponse()),
                        accessToken -> {
                            final ResponseCookie cookie = cookieUtils.generateCookie(properties.cookie(), accessToken);
                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                    .body(new SuccessResponse("Spotify access token refreshed successfully"));
                        }
                );

    }
}

