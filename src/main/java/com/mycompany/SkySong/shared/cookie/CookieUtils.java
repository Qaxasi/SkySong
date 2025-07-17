package com.mycompany.SkySong.shared.cookie;

import com.mycompany.SkySong.config.cookie.CookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class CookieUtils {
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public  ResponseCookie generateCookie(CookieProperties cookieProperties, String value) {
        return ResponseCookie.from(cookieProperties.name(), value)
                .path(cookieProperties.path())
                .maxAge(cookieProperties.maxAge())
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .sameSite(cookieProperties.sameSite())
                .build();
    }
}

