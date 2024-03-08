package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.auth.security.TokenGenerator;
import org.springframework.stereotype.Service;

@Service
public class UserSessionInitializerImpl implements UserSessionInitializer {

    private final SessionCreation sessionCreation;
    private final UserAuthProcessor authProcessor;
    private final TokenGenerator tokenGenerator;

    public UserSessionInitializerImpl(SessionCreation sessionCreation,
                                      UserAuthProcessor authProcessor, TokenGenerator tokenGenerator) {
        this.sessionCreation = sessionCreation;
        this.authProcessor = authProcessor;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Session initializeSession(LoginRequest request) {
        String token = tokenGenerator.generateToken();
        User user = authProcessor.fetchUserByAuthentication(request);
        return sessionCreation.createSession(token, user.getId());
    }
}
