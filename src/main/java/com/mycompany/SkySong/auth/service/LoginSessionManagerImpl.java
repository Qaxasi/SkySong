package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class LoginSessionManagerImpl implements LoginSessionManager {
    @Override
    public String initializeAndSaveSession(LoginRequest request) {
        return null;
    }
}
