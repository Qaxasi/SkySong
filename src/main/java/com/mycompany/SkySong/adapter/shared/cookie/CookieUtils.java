package com.mycompany.SkySong.adapter.shared.cookie;

import com.mycompany.SkySong.shared.config.cookie.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieFactory {

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateAccessTokenCookie(String value, )

    private ResponseCookie generateCookie(String value, CookieProperties cookieProperties) {
        return ResponseCookie.from(cookieProperties.getName(), value)
                .path(cookieProperties.getPath())
                .maxAge(cookieProperties.getMaxAge())
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .build();
    }
}
// inna nazwa dla pakietu ?
