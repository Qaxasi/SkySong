package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.client.SpotifyAuthorizationService;
import com.mycompany.SkySong.entity.SpotifyAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/auth")
public class TokenController {
    private final SpotifyAuthorizationService spotifyAuthorizationService;

    @Autowired
    public TokenController(SpotifyAuthorizationService spotifyTokenService) {
        this.spotifyAuthorizationService = spotifyTokenService;
    }

    @GetMapping("/token")
    public SpotifyAccessToken fetchToken(@RequestParam String code)  {
        return spotifyAuthorizationService.getAccessToken(code);
    }

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView(spotifyAuthorizationService.getAuthorizationCodeURL());
    }

}
