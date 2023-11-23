package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieDeleterImpl implements CookieDeleter {
    @Override
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {

    }
}
