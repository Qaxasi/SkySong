package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.service.AuthService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
}
