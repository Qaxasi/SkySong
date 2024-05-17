package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.domain.ports.SessionRepositoryPort;
import com.mycompany.SkySong.user.Session;
import java.util.Date;

class UserSessionCreator {
    private final SessionRepositoryPort sessionRepository;

    UserSessionCreator(SessionRepositoryPort sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void createUserSession(String sessionToken, int userId) {
        Session session = createSession(sessionToken, userId);
        sessionRepository.save(session);
    }

    private Session createSession(String hashedToken, Integer userId) {
        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userId);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        return session;
    }
}
