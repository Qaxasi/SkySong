package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.application.dto.LoginRequest;

class LoginService {

    private final TokenGenerator tokenGenerator;
    private final UserSessionCreator userSessionCreator;

   LoginService(TokenGenerator tokenGenerator, UserSessionCreator userSessionCreator) {
        this.tokenGenerator = tokenGenerator;
        this.userSessionCreator = userSessionCreator;
    }

    public String createSession(LoginRequest request) {
        String sessionToken = tokenGenerator.generateToken();
        userSessionCreator.createUserSession(request, sessionToken);
        return sessionToken;
    }
}
