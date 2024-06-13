package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.registration.domain.model.User;

class LoginHandler {

    private final UserSessionCreator userSessionCreator;
    private final UserRepositoryPort userRepository;
    private final AuthenticationPort userAuth;
    private final TokenHasher tokenHasher;
    private final TokenGenerator tokenGenerator;

   LoginHandler(TokenGenerator tokenGenerator,
                UserSessionCreator userSessionCreator,
                UserRepositoryPort userRepository,
                AuthenticationPort userAuth,
                TokenHasher tokenHasher) {
        this.tokenGenerator = tokenGenerator;
        this.userSessionCreator = userSessionCreator;
        this.userRepository = userRepository;
        this.userAuth = userAuth;
        this.tokenHasher = tokenHasher;
   }

    public String login(LoginRequest request) {
       String username = userAuth.authenticateUser(request.usernameOrEmail(), request.password());
       User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + username));

        String sessionToken = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.hashToken(sessionToken);
        userSessionCreator.createUserSession(hashedToken, user.getId());
        return sessionToken;
    }
}
