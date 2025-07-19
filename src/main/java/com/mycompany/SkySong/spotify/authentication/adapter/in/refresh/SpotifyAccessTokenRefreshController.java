package com.mycompany.SkySong.spotify.adapter.authentication.in.refresh;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.refresh.SpotifyAccessTokenRefresher;
import com.mycompany.SkySong.config.spotify.SpotifyAccessTokenCookieProperties;
import com.mycompany.SkySong.shared.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.infrastructure.context.UserContext;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyAccessTokenRefreshController {
    private final SpotifyAccessTokenRefresher accessTokenRefresher;
    private final CookieUtils cookieUtils;
    private final SpotifyAccessTokenCookieProperties properties;

    public SpotifyAccessTokenRefreshController(final SpotifyAccessTokenRefresher accessTokenRefresher,
                                               final CookieUtils cookieUtils,
                                               final SpotifyAccessTokenCookieProperties properties) {
        this.accessTokenRefresher = accessTokenRefresher;
        this.cookieUtils = cookieUtils;
        this.properties = properties;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshAccessToken() {
        final Integer userId = UserContext.getUserId();

        final Result<String> result = accessTokenRefresher.refresh(userId);
        if (result.isFailure()) {
            return ResponseEntity
                    .status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        final String newAccessToken = result.data();
        final ResponseCookie cookie = cookieUtils.generateCookie(properties.cookie(), newAccessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SuccessResponse("Access token refreshed successfully."));
    }
}
