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
    private final UserRetrieval userRetrieval;

    public LoginServiceImpl(SessionCreation sessionCreation,
                            ApplicationMessageService messageService,
                            UserAuthentication userAuth, UserRetrieval userRetrieval) {
        this.sessionCreation = sessionCreation;
        this.messageService = messageService;
        this.userAuth = userAuth;
        this.userRetrieval = userRetrieval;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication auth = userAuth.authenticateUser(loginRequest);
            User user = userRetrieval.findByAuthUsername(auth.getName());
            return sessionCreation.createSession(user.getId());
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }
}
