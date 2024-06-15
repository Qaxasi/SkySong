package com.mycompany.SkySong.security;

import com.mycompany.SkySong.common.dao.SessionDAO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionValidation {

    private final SessionDAO sessionDAO;

    private final SHA256TokenHasher SHA256TokenHasher;

    public SessionValidation(SessionDAO sessionDAO, SHA256TokenHasher SHA256TokenHasher) {
        this.sessionDAO = sessionDAO;
        this.SHA256TokenHasher = SHA256TokenHasher;
    }

    public boolean validateSession(String sessionId) {
        String hashedSessionId = SHA256TokenHasher.hashToken(sessionId);
        return sessionDAO.findById(hashedSessionId)
                .map(session -> session.getExpiresAt().after(new Date()))
                .orElse(false);
    }
}
