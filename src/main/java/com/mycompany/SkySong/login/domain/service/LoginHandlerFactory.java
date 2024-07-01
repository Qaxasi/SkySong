package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.domain.ports.LoginSessionRepository;
import com.mycompany.SkySong.login.domain.ports.LoginUserRepository;
import com.mycompany.SkySong.login.domain.ports.UserAuthentication;

public class LoginHandlerFactory {

    public LoginHandler createLoginHandler(LoginSessionRepository sessionRepository,
                                           UserAuthentication userAuth,
                                           LoginUserRepository userRepository) {
        SessionTokenGenerator tokenGenerator = new SessionTokenGenerator();
        UserSessionCreator sessionCreator = new UserSessionCreator(sessionRepository, tokenGenerator);
        return new LoginHandler(sessionCreator, userAuth, userRepository);
    }
}
