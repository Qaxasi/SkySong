package com.mycompany.SkySong.application.login.usecase;

import com.mycompany.SkySong.application.login.exception.UserNotFoundException;
import com.mycompany.SkySong.application.login.dto.LoginRequest;
import com.mycompany.SkySong.domain.login.service.UserSessionCreator;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.login.ports.LoginUserRepository;
import com.mycompany.SkySong.domain.login.ports.UserAuthentication;

public class LoginHandler {
    private final UserSessionCreator sessionCreator;
    private final UserAuthentication auth;
    private final LoginUserRepository userRepository;

    public LoginHandler(UserSessionCreator sessionCreator,
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
