package com.mycompany.SkySong.common.security;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.user.SessionDAO;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionValidation {

    private final SessionDAO sessionDAO;

    private final TokenHasher tokenHasher;

    public SessionValidation(SessionDAO sessionDAO, TokenHasher tokenHasher) {
        this.sessionDAO = sessionDAO;
        this.tokenHasher = tokenHasher;
    }

    public boolean validateSession(String sessionId) {
        String hashedSessionId = tokenHasher.generateHashedToken(sessionId);
        return sessionDAO.findById(hashedSessionId)
                .map(session -> session.getExpiresAt().after(new Date()))
                .orElse(false);
    }
}
