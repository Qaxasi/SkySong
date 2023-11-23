package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieDeleter {
    void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name);
}
