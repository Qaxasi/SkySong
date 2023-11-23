package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletResponse;

public interface AddCookie {
    void addCookie(HttpServletResponse response, String name, String value, int maxAge);
}
