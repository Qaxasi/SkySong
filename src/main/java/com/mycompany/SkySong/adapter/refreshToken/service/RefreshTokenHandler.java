package com.mycompany.SkySong.adapter.refreshToken.service;

import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetailsService;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenHandler {

    private final JwtTokenManager tokenManager;
    private final CustomUserDetailsService userDetailsService;

    public RefreshTokenHandler(JwtTokenManager tokenManager,
                               CustomUserDetailsService userDetailsService) {
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        String username = tokenManager.extractUsername(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        return tokenManager.generateToken(userDetails);
    }

    public boolean validateRefreshToken(String token) {
        return tokenManager.isTokenValid(token);
    }
}
