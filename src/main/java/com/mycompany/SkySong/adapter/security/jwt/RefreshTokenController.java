package com.mycompany.SkySong.adapter.security.jwt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class RefreshTokenController {
    private final JwtTokenManager tokenManager;
    private final CookieUtils cookieUtils;

    public RefreshTokenController(JwtTokenManager tokenManager,
                                  CookieUtils cookieUtils) {
        this.tokenManager = tokenManager;
        this.cookieUtils = cookieUtils;
    }
}
