package com.mycompany.SkySong.domain.login.service;

import com.mycompany.SkySong.domain.login.ports.LoginSessionRepository;

public class UserSessionCreatorFactory {
    public UserSessionCreator createUserSessionCreator(LoginSessionRepository sessionRepository) {
        SessionTokenGenerator tokenGenerator = new SessionTokenGenerator();
        return new UserSessionCreator(sessionRepository, tokenGenerator);
    }
}
