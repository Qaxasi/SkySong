package com.mycompany.SkySong.adapter.music.spotify.authentication.in;

import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.music.authentication.usecase.MusicServiceAuthUseCase;
import com.mycompany.SkySong.domain.music.authentication.dto.AuthParams;
import com.mycompany.SkySong.infrastructure.context.UserContext;
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
    public ResponseEntity<ApiResponse> handleCallback(@RequestParam("code") String authCode) {
        // Pobieranie userId z kontekstu użytkownika
        Integer userId = UserContext.getUserId();

        // Tworzenie obiektu AuthParams
        AuthParams params = new AuthParams(authCode);

        try {
            // Wywołanie use case
            String accessToken = authUseCase.authenticateUser(userId, params);

            // Generowanie cookie z accessToken
            ResponseCookie cookie = cookieUtils.generateCookie("spotifyAccessToken", accessToken, "/api/v1/spotify", 3600);

            // Zwrot odpowiedzi sukcesu
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new ApiResponse("Spotify authorization successful."));
        } catch (Exception ex) {
            // Obsługa błędów (np. walidacja, brak odpowiedzi od API itp.)
            return ResponseEntity.status(500)
                    .body(new ApiResponse("We encountered an issue processing your request. Please try again later."));
        }
    }
}