package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.TokenGenerator;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {

    private final TokenGenerator tokenGenerator;
    private final UserSessionCreator userSessionCreator;

    public UserSessionService(TokenGenerator tokenGenerator, UserSessionCreator userSessionCreator) {
        this.tokenGenerator = tokenGenerator;
        this.userSessionCreator = userSessionCreator;
    }

    public String createSession(LoginRequest request) {
        String sessionToken = tokenGenerator.generateToken();
        userSessionCreator.createUserSession(request, sessionToken);
        return sessionToken;
    }
}
