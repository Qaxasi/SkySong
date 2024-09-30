package com.mycompany.SkySong.adapter.spotify.controller;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.spotify.authentication.SpotifyTokenHandler;
import com.mycompany.SkySong.adapter.spotify.authentication.SpotifyTokenRedisHandler;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spotify/auth")
public class SpotifyCallbackController {

    private final SpotifyTokenHandler tokenHandler;
    private final JwtTokenManager jwtTokenManager;

    private final SpotifyTokenRedisHandler redisHandler;

    public SpotifyCallbackController(SpotifyTokenHandler tokenHandler,
                                     JwtTokenManager jwtTokenManager,
                                     SpotifyTokenRedisHandler redisHandler) {
        this.tokenHandler = tokenHandler;
        this.jwtTokenManager = jwtTokenManager;
        this.redisHandler = redisHandler;
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleSpotifyCallback(@RequestParam("code") String authCode,
                                                        @CookieValue(name = "jwtToken") String jwtToken,
                                                        HttpServletResponse response) {
        int userId = jwtTokenManager.extractUserId(jwtToken);

        SpotifyAccessTokenResponse spotifyResponse = tokenHandler.getAccessToken(authCode);

        redisHandler.saveRefreshToken(userId, spotifyResponse.refreshToken());

        Cookie accessTokenCookie = new Cookie("spotifyAccessToken", spotifyResponse.accessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/api/v1/spotify/");
        accessTokenCookie.setMaxAge(3600);
        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok("Spotify authorization successful.");
    }
}
