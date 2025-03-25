package com.mycompany.SkySong.adapter.music.spotify.authentication.in.access;

import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.music.authentication.usecase.MusicServiceAuthUseCase;
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

    private final MusicServiceAuthUseCase authUseCase;
    private final CookieUtils cookieUtils;

    public SpotifyCallbackController(MusicServiceAuthUseCase authUseCase,
                                     CookieUtils cookieUtils) {
        this.authUseCase = authUseCase;
        this.cookieUtils = cookieUtils;
    }


    @GetMapping("/callback")
    public ResponseEntity<Object> handleCallback(@RequestParam("code") String authCode) {
        Integer userId = UserContext.getUserId();
        AuthParams params = new AuthParams(authCode);

        Result<String> authResult = authUseCase.authenticateUser(userId, params);
        if (authResult.isFailure()) {
            return ResponseEntity
                    .status(authResult.errorType().getHttpStatus())
                    .body(authResult.toErrorResponse());
        }

        String accessToken = authResult.data();
        ResponseCookie cookie = cookieUtils.generateCookie(
                "spotifyAccessToken",
                accessToken,
                "/api/v1/spotify",
                3600);


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Spotify authorization successful."));
    }
}