package com.mycompany.SkySong.adapter.spotify.controller;

import com.mycompany.SkySong.adapter.spotify.authentication.SpotifyTokenHandler;
import com.mycompany.SkySong.adapter.spotify.dto.SpotifyAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spotify/token")
public class SpotifyRefreshTokenController {

    private final SpotifyTokenHandler tokenHandler;

    public SpotifyRefreshTokenController(SpotifyTokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @PostMapping("/refresh")
    public ResponseEntity<SpotifyAccessTokenResponse> refreshAccessToken(@RequestParam("refresh_token") String token) {
        return ResponseEntity.ok(tokenHandler.refreshAccessToken(token));
    }
}
