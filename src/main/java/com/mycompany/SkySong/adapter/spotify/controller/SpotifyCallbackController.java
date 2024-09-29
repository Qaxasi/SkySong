package com.mycompany.SkySong.adapter.spotify.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.SpotifyTokenHandler;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spotify/auth")
public class SpotifyCallbackController {

    private final SpotifyTokenHandler tokenHandler;

    public SpotifyCallbackController(SpotifyTokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @GetMapping("/callback")
    public ResponseEntity<SpotifyAccessTokenResponse> handleSpotifyCallback(@RequestParam("code") String authCode) {
        SpotifyAccessTokenResponse response = tokenHandler.getAccessToken(authCode);
        return ResponseEntity.ok(response);
    }
}
