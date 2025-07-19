package com.mycompany.SkySong.spotify.adapter.authentication.in.access;

import com.mycompany.SkySong.spotify.adapter.authentication.in.access.SpotifyAuthUrlGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/spotify/auth")
public class SpotifyLoginController {

    private final SpotifyAuthUrlGenerator authUrlHandler;

    public SpotifyLoginController(final SpotifyAuthUrlGenerator authUrlHandler) {
        this.authUrlHandler = authUrlHandler;
    }

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView(authUrlHandler.getAuthorizationCodeUrl());
    }
}
