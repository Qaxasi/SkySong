package com.mycompany.SkySong.adapter.refreshToken.handler;

import com.mycompany.SkySong.adapter.refreshToken.exception.InvalidRefreshTokenException;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetailsService;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            throw new InvalidRefreshTokenException("Refresh token is invalid or expired.");
        }

        String username = tokenManager.extractUsername(refreshToken);

        try {
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return tokenManager.generateRefreshToken(userDetails);
        } catch (UsernameNotFoundException ex) {
            throw new InvalidRefreshTokenException("Session renewal failed: please log in again.");
        }
    }

    private boolean validateToken(String token) {
        return tokenManager.isTokenValid(token);
    }
}