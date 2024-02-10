package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;

@Service
public class SessionDeletionImpl implements SessionDeletion {

    private final SessionDAO sessionDAO;

    private final TokenHasher tokenHasher;

    public SessionDeletionImpl(SessionDAO sessionDAO, TokenHasher tokenHasher) {
        this.sessionDAO = sessionDAO;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public void deleteSession(String sessionId) {
        String hashedSessionId = tokenHasher.generateHashedToken(sessionId);
        sessionDAO.deleteById(hashedSessionId);
    }
}
