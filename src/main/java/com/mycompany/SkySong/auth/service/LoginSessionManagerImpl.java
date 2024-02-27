package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.SessionPersistence;
import org.springframework.stereotype.Service;

@Service
public class LoginSessionManagerImpl implements LoginSessionManager {
    
    private final SessionPersistence sessionPersistence;
    private final UserSessionInitializer sessionInit;

    public LoginSessionManagerImpl(SessionPersistence sessionPersistence, UserSessionInitializer sessionInit) {
        this.sessionPersistence = sessionPersistence;
        this.sessionInit = sessionInit;
    }

    @Override
    public String initializeAndSaveSession(LoginRequest request) {
        return null;
    }
}
