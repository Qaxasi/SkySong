package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginSessionManager sessionManager;

    public LoginServiceImpl(LoginSessionManager sessionManager) {
        this.sessionManager = sessionManager;

    }

    @Override
    public Session login(LoginRequest request) {
        return sessionManager.initializeAndSaveSession(request);
    }
}
