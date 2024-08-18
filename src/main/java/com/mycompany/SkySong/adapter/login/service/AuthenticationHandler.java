package com.mycompany.SkySong.adapter.login.service;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationHandler {

    private final AuthenticationManager authManager;
    private final JwtTokenManager tokenManager;

    public AuthenticationHandler(AuthenticationManager authManager,
                                 JwtTokenManager tokenManager) {
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }
}
