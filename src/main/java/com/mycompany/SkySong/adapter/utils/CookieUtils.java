package com.mycompany.SkySong.adapter.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
public class CookieUtils {

    public String getJwtFromCookies(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateCookie(String name, String value, String path, long maxAge) {
        return ResponseCookie.from(name, value).path(path).maxAge(maxAge).httpOnly(true).build();
    }
}
