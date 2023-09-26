package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.service.AuthService;
import com.mycompany.SkySong.user.entity.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String login(LoginResponse loginResponse) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginResponse.usernameOrEmail(), loginResponse.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "User Logged-in successfully";
    }
}
