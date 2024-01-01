package com.mycompany.SkySong.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl {
    private final AuthenticationManager authenticationManager;

    public UserAuthenticationServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
