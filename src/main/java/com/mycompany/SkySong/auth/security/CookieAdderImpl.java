package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieAdderImpl implements CookieAdder {
    @Override
    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {

    }
}
