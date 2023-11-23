package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface CookieRetriever {
    Optional<Cookie> getCookie(HttpServletRequest request, String name);
}
