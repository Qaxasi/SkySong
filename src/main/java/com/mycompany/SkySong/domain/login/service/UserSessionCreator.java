package com.mycompany.SkySong.domain.login.service;

import com.mycompany.SkySong.domain.login.ports.LoginSessionRepository;
import com.mycompany.SkySong.domain.shared.entity.Session;
import java.util.Date;

public class UserSessionCreator {
    private final LoginSessionRepository sessionRepository;
    private final SessionTokenGenerator tokenGenerator;

    public UserSessionCreator(LoginSessionRepository sessionRepository,
                              SessionTokenGenerator tokenGenerator) {
        this.sessionRepository = sessionRepository;
        this.tokenGenerator = tokenGenerator;
    }

    public String createUserSession(int userId) {
        String token = tokenGenerator.generateToken();
        Session session = createSession(token, userId);
        sessionRepository.save(session);
        return token;
    }

    private Session createSession(String token, Integer userId) {
        Session session = new Session();
        session.setSessionId(token);
        session.setUserId(userId);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        return session;
    }
}
