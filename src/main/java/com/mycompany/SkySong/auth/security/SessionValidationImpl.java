package com.mycompany.SkySong.auth.security;

public class SessionValidationImpl implements SessionValidation {
    @Override
    public boolean validateSession(String sessionId) {
        return false;
    }
}
