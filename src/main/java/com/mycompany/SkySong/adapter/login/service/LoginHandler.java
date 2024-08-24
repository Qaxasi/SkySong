package com.mycompany.SkySong.adapter.login.service;

import com.mycompany.SkySong.adapter.login.dto.LoginResponse;
import com.mycompany.SkySong.adapter.security.CustomUserDetails;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.login.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginHandler {

    private final AuthenticationManager authManager;
    private final JwtTokenManager tokenManager;

    public LoginHandler(AuthenticationManager authManager,
                        JwtTokenManager tokenManager) {
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwtToken = tokenManager.generateToken(userDetails);
        String refreshToken = tokenManager.generateRefreshToken(userDetails);

        return new LoginResponse(jwtToken, refreshToken);
    }
}
