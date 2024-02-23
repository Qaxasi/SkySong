package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.auth.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final SessionCreation sessionCreation;
    private final ApplicationMessageService messageService;
    private final UserAuthentication userAuth;
    private final UserRetrieval retrieval;

    public LoginServiceImpl(SessionCreation sessionCreation,
                            ApplicationMessageService messageService,
                            UserAuthentication userAuth, UserRetrieval retrieval) {
        this.sessionCreation = sessionCreation;
        this.messageService = messageService;
        this.userAuth = userAuth;
        this.retrieval = retrieval;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication auth = userAuth.authenticateUser(loginRequest);
            User user = retrieval.findUserByAuthentication(auth);
            return sessionCreation.createSession(user.getId());
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }
}
