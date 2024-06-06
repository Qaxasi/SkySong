package com.mycompany.SkySong.logout.domain.service;

import com.mycompany.SkySong.logout.domain.ports.SessionRepository;

class LogoutHandler {

    private final SessionRepository sessionRepository;

    LogoutHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
