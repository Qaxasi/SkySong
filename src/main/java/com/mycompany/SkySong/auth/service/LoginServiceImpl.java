package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginSessionManager sessionManager;

    public LoginServiceImpl(LoginSessionManager sessionManager) {
        this.sessionManager = sessionManager;

    }

    @Override
    public String login(LoginRequest request) {
        return sessionManager.initializeAndSaveSession(request);
    }
}
