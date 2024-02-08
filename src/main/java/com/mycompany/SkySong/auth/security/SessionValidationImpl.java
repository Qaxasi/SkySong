package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionValidationImpl implements SessionValidation {

    private final SessionDAO sessionDAO;

    private final SecureTokenGenerator tokenGenerator;

    public SessionValidationImpl(SessionDAO sessionDAO, SecureTokenGenerator tokenGenerator) {
        this.sessionDAO = sessionDAO;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public boolean validateSession(String sessionId) {
        String hashedSessionId = tokenGenerator.generateHashedToken(sessionId);
        return sessionDAO.findById(hashedSessionId)
                .map(session -> session.getExpiresAt().after(new Date()))
                .orElse(false);
    }
}
