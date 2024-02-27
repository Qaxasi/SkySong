package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.stereotype.Service;

@Service
public class UserSessionInitializerImpl implements UserSessionInitializer {
    @Override
    public Session initializeSession(LoginRequest request) {
        return null;
    }
}
