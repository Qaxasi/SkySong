package com.mycompany.SkySong.auth.security;

public interface SessionValidation {
    boolean validateSession(String sessionId);
}
