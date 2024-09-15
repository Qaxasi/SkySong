package com.mycompany.SkySong.adapter.refreshToken.handler;

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
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token.");
        }
        String username = tokenManager.extractUsername(refreshToken);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return tokenManager.generateToken(userDetails);
    }

    private boolean validateToken(String token) {
        return tokenManager.isTokenValid(token);
    }
}
