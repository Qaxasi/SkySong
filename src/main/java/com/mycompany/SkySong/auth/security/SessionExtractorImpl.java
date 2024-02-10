package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;

public class SessionExtractorImpl implements SessionExtractor {
    
    @Override
    public String getSessionIdFromRequest(HttpServletRequest request) {
        return null;
    }
}
