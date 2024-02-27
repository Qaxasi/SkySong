package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final LoginSessionManager sessionManager;

    public LoginServiceImpl(LoginSessionManager sessionManager) {
        this.sessionManager = sessionManager;

    }

    @Override
    public String login(LoginRequest request) {
        return sessionManager.initializeAndSaveSession(request);
    }
}
