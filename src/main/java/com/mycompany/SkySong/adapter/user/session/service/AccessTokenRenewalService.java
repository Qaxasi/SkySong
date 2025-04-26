package com.mycompany.SkySong.adapter.identity.session.service;

import com.mycompany.SkySong.adapter.identity.session.exception.InvalidRefreshTokenException;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetailsService;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.shared.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccessTokenRenewalService {
    private final JwtTokenManager jwtManager;
    private final CustomUserDetailsService userDetailsService;

    public AccessTokenRenewalService(JwtTokenManager jwtManager,
                                     CustomUserDetailsService userDetailsService) {
        this.jwtManager = jwtManager;
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        jwtManager.validateToken(refreshToken);

        String username = jwtManager.extractUsername(refreshToken);

        try {
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtManager.generateToken(userDetails);
        } catch (UsernameNotFoundException ex) {
            log.warn("Refresh token valid, but user '{}' not found", username);
            throw new InvalidRefreshTokenException(
                    "Session renewal failed. Please log in again.",
                    ErrorType.INVALID_REFRESH_TOKEN);
        }
    }
}