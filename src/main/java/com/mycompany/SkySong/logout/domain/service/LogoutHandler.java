package com.mycompany.SkySong.logout.domain.service;

import com.mycompany.SkySong.logout.domain.ports.SessionRepository;
import com.mycompany.SkySong.logout.domain.ports.SessionTokenHasher;

class LogoutHandler {

    private final SessionRepository sessionRepository;
    private final SessionTokenHasher tokenHasher;

    LogoutHandler(SessionRepository sessionRepository,
                  SessionTokenHasher tokenHasher) {
        this.sessionRepository = sessionRepository;
        this.tokenHasher = tokenHasher;
    }

    public void deleteSession(String sessionId) {
        String hashedSessionId = tokenHasher.hashToken(sessionId);
        sessionRepository.deleteById(hashedSessionId);
    }
}
