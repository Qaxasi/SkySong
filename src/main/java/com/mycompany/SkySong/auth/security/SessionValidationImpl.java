package com.mycompany.SkySong.auth.security;

import org.springframework.stereotype.Service;

@Service
public class SessionValidationImpl implements SessionValidation {
    @Override
    public boolean validateSession(String sessionId) {
        return false;
    }
}
