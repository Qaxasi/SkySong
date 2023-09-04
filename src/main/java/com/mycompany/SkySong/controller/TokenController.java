package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.client.SpotifyTokenService;
import com.mycompany.SkySong.entity.SpotifyAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class TokenController {
    private SpotifyTokenService spotifyTokenService;

    @Autowired
    public TokenController(SpotifyTokenService spotifyTokenService) {
        this.spotifyTokenService = spotifyTokenService;
    }

    @GetMapping("/token")
    public SpotifyAccessToken fetchToken() throws Exception {
        return spotifyTokenService.getAccessToken();
    }
}
