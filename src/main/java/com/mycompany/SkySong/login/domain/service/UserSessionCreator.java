package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.domain.ports.SessionRepository;
import com.mycompany.SkySong.common.entity.Session;
import java.util.Date;

class UserSessionCreator {
    private final SessionRepository sessionRepository;
    private final SessionTokenGenerator tokenGenerator;

    UserSessionCreator(SessionRepository sessionRepository,
                       SessionTokenGenerator tokenGenerator) {
        this.sessionRepository = sessionRepository;
        this.tokenGenerator = tokenGenerator;
    }

    public void createUserSession(String hashedToken, int userId) {
        Session session = createSession(hashedToken, userId);
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
