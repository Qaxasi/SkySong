package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.login.domain.ports.UserAuthenticationPort;
import com.mycompany.SkySong.login.domain.ports.UserRepositoryPort;
import com.mycompany.SkySong.registration.domain.model.User;

class LoginService {

    private final TokenGenerator tokenGenerator;
    private final UserSessionCreator userSessionCreator;
    private final UserRepositoryPort userRepository;
    private final UserAuthenticationPort userAuth;

   LoginService(TokenGenerator tokenGenerator,
                UserSessionCreator userSessionCreator,
                UserRepositoryPort userRepository,
                UserAuthenticationPort userAuth) {
        this.tokenGenerator = tokenGenerator;
        this.userSessionCreator = userSessionCreator;
        this.userRepository = userRepository;
        this.userAuth = userAuth;
   }

    public String login(LoginRequest request) {
       String username = userAuth.authenticateUser(request);
       User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + username));

        String sessionToken = tokenGenerator.generateToken();
        userSessionCreator.createUserSession(sessionToken, user.getId());
        return sessionToken;
    }
}
