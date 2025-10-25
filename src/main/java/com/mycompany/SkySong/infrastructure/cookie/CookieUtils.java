package com.mycompany.SkySong.infrastructure.cookie;

import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.time.Duration;

@Component
public class CookieUtils {
    public Result<String> getCookieValue(final HttpServletRequest request, final String cookieName) {
        final Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isBlank()) {
            return Result.failure("Cookie is missing or empty", ErrorType.COOKIE_NOT_FOUND);
        } else {
            return Result.success(cookie.getValue());
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

