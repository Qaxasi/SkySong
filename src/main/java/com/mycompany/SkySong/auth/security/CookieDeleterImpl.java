package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieDeleterImpl implements CookieDeleter {
    @Override
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {

    }
}
