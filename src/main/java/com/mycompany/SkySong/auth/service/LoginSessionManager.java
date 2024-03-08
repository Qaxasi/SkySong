package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginSessionManagerImpl implements LoginSessionManager {

    private final UserSessionInitializer sessionInit;

    public LoginSessionManagerImpl(UserSessionInitializer sessionInit) {
        this.sessionInit = sessionInit;
    }

    @Override
    @Transactional
    public Session initializeAndSaveSession(LoginRequest request) {
        return sessionInit.initializeSession(request);
    }
}
