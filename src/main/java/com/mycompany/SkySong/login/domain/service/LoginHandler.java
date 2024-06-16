package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.domain.exception.UserNotFoundException;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.login.domain.ports.LoginUserRepository;
import com.mycompany.SkySong.login.domain.ports.UserAuthentication;

class LoginHandler {
    private final UserSessionCreator sessionCreator;
    private final UserAuthentication auth;
    private final LoginUserRepository userRepository;

    LoginHandler(UserSessionCreator sessionCreator,
                 UserAuthentication auth,
                 LoginUserRepository userRepository) {
        this.sessionCreator = sessionCreator;
        this.auth = auth;
        this.userRepository = userRepository;
    }

    public String login(LoginRequest request) {
       String username = auth.authenticateUser(request.usernameOrEmail(), request.password());
       User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + username));

       String sessionToken = sessionCreator.createUserSession(user.getId());
       return sessionToken;
    }
}
