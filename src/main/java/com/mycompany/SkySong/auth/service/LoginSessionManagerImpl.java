package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.security.SessionSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginSessionManagerImpl implements LoginSessionManager {

    private final SessionSaver sessionSaver;
    private final UserSessionInitializer sessionInit;

    public LoginSessionManagerImpl(SessionSaver sessionSaver, UserSessionInitializer sessionInit) {
        this.sessionSaver = sessionSaver;
        this.sessionInit = sessionInit;
    }

    @Override
    @Transactional
    public String initializeAndSaveSession(LoginRequest request) {
        Session session = sessionInit.initializeSession(request);
        sessionSaver.saveSession(session);
        return session.getSessionId();
    }
}
