package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.service.AuthService;
import com.mycompany.SkySong.user.entity.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String login(LoginResponse loginResponse) {
        return null;
    }
}
