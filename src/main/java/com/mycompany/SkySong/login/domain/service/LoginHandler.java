package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.login.domain.ports.UserAuthentication;

class LoginHandler {
    private final UserSessionCreator sessionCreator;
    private final UserAuthentication authentication;

    LoginHandler(UserSessionCreator sessionCreator, UserAuthentication authentication) {
        this.sessionCreator = sessionCreator;
        this.authentication = authentication;
    }

    public String login(LoginRequest request) {
       String username = userAuth.authenticateUser(request.usernameOrEmail(), request.password());
       User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + username));

        String sessionToken = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.hashToken(sessionToken);
        sessionCreator.createUserSession(hashedToken, user.getId());
        return sessionToken;
    }
}
