package com.mycompany.SkySong.logout.domain.service;

import com.mycompany.SkySong.logout.domain.ports.SessionRepository;

class SessionDeleter {

    private final SessionRepository sessionRepository;

    SessionDeleter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
