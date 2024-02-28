package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.security.SessionCreation;
import org.springframework.stereotype.Service;

@Service
public class UserSessionInitializerImpl implements UserSessionInitializer {

    private final SessionCreation sessionCreation;
    private final UserAuthProcessor authManager;

    public UserSessionInitializerImpl(SessionCreation sessionCreation, UserAuthProcessor authManager) {
        this.sessionCreation = sessionCreation;
        this.authManager = authManager;
    }

    @Override
    public Session initializeSession(LoginRequest request) {
        User user = authManager.fetchUserByAuthentication(request);
        return sessionCreation.createSession(user.getId());
    }
}
