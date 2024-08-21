package com.mycompany.SkySong.adapter.security.jwt;

import com.mycompany.SkySong.adapter.security.CustomUserDetails;
import com.mycompany.SkySong.adapter.security.CustomUserDetailsService;

public class RefreshTokenHandler {

    private final JwtTokenManager tokenManager;
    private final CustomUserDetailsService userDetailsService;

    public RefreshTokenHandler(JwtTokenManager tokenManager,
                               CustomUserDetailsService userDetailsService) {
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
    }
}
