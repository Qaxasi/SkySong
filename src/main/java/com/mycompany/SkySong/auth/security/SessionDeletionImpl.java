package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;

@Service
public class SessionDeletionImpl implements SessionDeletion {

    private final SessionDAO sessionDAO;

    private final SecureTokenGenerator tokenGenerator;

    public SessionDeletionImpl(SessionDAO sessionDAO, SecureTokenGenerator tokenGenerator) {
        this.sessionDAO = sessionDAO;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public void deleteSession(String sessionId) {
        String hashedSessionId = tokenGenerator.generateHashedToken(sessionId);
        sessionDAO.deleteById(hashedSessionId);
    }
}
