package com.mycompany.SkySong.spotify.adapter.authentication.in.access;

import com.mycompany.SkySong.adapter.music.spotify.authentication.out.access.SpotifyAuthorizationService;
import com.mycompany.SkySong.config.spotify.SpotifyAccessTokenCookieProperties;
import com.mycompany.SkySong.shared.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.infrastructure.context.UserContext;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/music/auth")
public class SpotifyCallbackController {
    private final SpotifyAuthorizationService spotifyAuth;
    private final CookieUtils cookieUtils;
    private final SpotifyAccessTokenCookieProperties properties;

    public SpotifyCallbackController(final SpotifyAuthorizationService spotifyAuth,
                                     final CookieUtils cookieUtils,
                                     final SpotifyAccessTokenCookieProperties properties) {
        this.spotifyAuth = spotifyAuth;
        this.cookieUtils = cookieUtils;
        this.properties = properties;
    }


    @GetMapping("/callback")
    public ResponseEntity<BaseResponse> handleCallback(@RequestParam("code") final String authCode) {
        final Integer userId = UserContext.getUserId();
        final AuthParams params = new AuthParams(authCode);

        final Result<String> authResult = spotifyAuth.authenticateAndReturnToken(userId, params);
        if (authResult.isFailure()) {
            return ResponseEntity
                    .status(authResult.errorType().getHttpStatus())
                    .body(authResult.toErrorResponse());
        }

        final String accessToken = authResult.data();
        final ResponseCookie cookie = cookieUtils.generateCookie(properties.cookie(), accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SuccessResponse("Spotify authorization successful."));
    }
}