package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;

public interface SessionExtractor {

    String getSessionIdFromRequest(HttpServletRequest request);
}
