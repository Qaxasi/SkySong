package com.mycompany.SkySong.authentication.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface CookieService {
    Optional<Cookie> getCookie(HttpServletRequest request, String name);
    void addCookie(HttpServletResponse response, String name, String value, int maxAge);
    void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name);
}
