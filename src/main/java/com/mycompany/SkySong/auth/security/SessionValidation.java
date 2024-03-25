package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionValidationImpl implements SessionValidation {

    private final SessionDAO sessionDAO;

    private final TokenHasher tokenHasher;

    public SessionValidationImpl(SessionDAO sessionDAO, TokenHasher tokenHasher) {
        this.sessionDAO = sessionDAO;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public boolean validateSession(String sessionId) {
        String hashedSessionId = tokenHasher.generateHashedToken(sessionId);
        return sessionDAO.findById(hashedSessionId)
                .map(session -> session.getExpiresAt().after(new Date()))
                .orElse(false);
    }
}
