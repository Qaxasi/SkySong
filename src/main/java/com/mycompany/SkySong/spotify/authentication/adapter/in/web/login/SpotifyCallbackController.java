package com.mycompany.SkySong.spotify.authentication.adapter.in.web.login;

import com.mycompany.SkySong.spotify.authentication.application.login.SpotifyAuthorization;
import com.mycompany.SkySong.infrastructure.spotify.config.SpotifyAccessTokenCookieProperties;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.infrastructure.context.UserContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/music/auth")
public class SpotifyCallbackController {
    private final SpotifyAuthorization spotifyAuth;
    private final CookieUtils cookieUtils;
    private final SpotifyAccessTokenCookieProperties properties;

    public SpotifyCallbackController(final SpotifyAuthorization spotifyAuth,
                                     final CookieUtils cookieUtils,
                                     final SpotifyAccessTokenCookieProperties properties) {
        this.spotifyAuth = spotifyAuth;
        this.cookieUtils = cookieUtils;
        this.properties = properties;
    }


    @GetMapping("/callback")
    public ResponseEntity<BaseResponse> handleCallback(@RequestParam("code") final String authCode) {
        final Integer userId = UserContext.getUserId();

        return spotifyAuth.authenticateAndReturnToken(userId, authCode)
                .fold(
                        error -> ResponseEntity
                                .status(error.errorType().getHttpStatus())
                                .body(error.toErrorResponse()),
                        accessToken -> {
                            final ResponseCookie cookie = cookieUtils.generateCookie(properties.cookie(), accessToken);
                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                    .body(new SuccessResponse("Spotify authorization successful."));
                        }
                );
    }
}
