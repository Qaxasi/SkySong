package com.mycompany.SkySong.identity.adapter.in.web.cookie;

import com.mycompany.SkySong.identity.config.CookieProperties;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.time.Duration;

@Component
public class CookieUtils {
    public Result<String> getCookieValue(final HttpServletRequest request,
                                         final CookieProperties properties) {

        final Cookie cookie = WebUtils.getCookie(request, properties.name());
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isBlank()) {
            return Result.failure("Cookie is missing or empty", ErrorType.INVALID_COOKIE);
        } else {
            return Result.success(cookie.getValue());
        }
    }
    public ResponseCookie generateCookie(final CookieProperties properties,
                                         final String value,
                                         final Duration ttl) {
        final long seconds = (ttl == null) ? 0 : ttl.getSeconds();
        final int maxAge = Math.toIntExact(seconds);

        return ResponseCookie.from(properties.name(), value)
                .path(properties.path())
                .maxAge(maxAge)
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .sameSite(properties.sameSite())
                .build();
    }
}

