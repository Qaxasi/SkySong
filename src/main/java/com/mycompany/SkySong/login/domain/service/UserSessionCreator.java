package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.login.domain.ports.SessionRepositoryPort;
import com.mycompany.SkySong.user.Session;
import java.util.Date;

class UserSessionCreator {

    private final TokenHasher tokenHasher;
    private final SessionRepositoryPort sessionRepository;

    UserSessionCreator(TokenHasher tokenHasher,
                       SessionRepositoryPort sessionRepository) {
        this.tokenHasher = tokenHasher;
        this.sessionRepository = sessionRepository;
    }

    public void createUserSession(String sessionToken, int userId) {
        Session session = createSession(sessionToken, userId);
        sessionRepository.save(session);
    }

    private Session createSession(String token, Integer userId) {
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userId);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        return session;
    }
}
