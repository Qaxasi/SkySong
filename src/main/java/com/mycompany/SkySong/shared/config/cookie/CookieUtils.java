package com.mycompany.SkySong.adapter.shared.cookie;

import com.mycompany.SkySong.shared.config.cookie.CookieProperties;
import com.mycompany.SkySong.shared.config.security.jwt.JwtAccessTokenProperties;
import com.mycompany.SkySong.shared.config.security.jwt.JwtRefreshTokenProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtils {

    private final JwtAccessTokenProperties accessTokenProperties;
    private final JwtRefreshTokenProperties refreshTokenProperties;

    public CookieUtils(JwtAccessTokenProperties accessTokenProperties, JwtRefreshTokenProperties refreshTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    private ResponseCookie generateCookie(String value, CookieProperties cookieProperties) {
        return ResponseCookie.from(cookieProperties.getName(), value)
                .path(cookieProperties.getPath())
                .maxAge(cookieProperties.getMaxAge())
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .build();
    }

    public ResponseCookie generateAccessTokenCookie(String token) {
        return generateCookie(token, accessTokenProperties.getCookieProperties());
    }

    public ResponseCookie generateRefreshTokenCookie(String token) {
        return generateCookie(token, refreshTokenProperties.getCookieProperties());
    }
}

