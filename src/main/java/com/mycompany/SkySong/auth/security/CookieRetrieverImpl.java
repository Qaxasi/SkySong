package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

class CookieRetrieverImpl implements CookieRetriever {
    @Override
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        return Optional.empty();
    }
}
