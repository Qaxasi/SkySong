package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final SessionCreation sessionCreation;
    private final ApplicationMessageService messageService;
    private final UserAuthenticationService userAuth;

    public LoginServiceImpl(SessionCreation sessionCreation, ApplicationMessageService messageService,
                            UserAuthenticationService userAuth) {
        this.sessionCreation = sessionCreation;
        this.messageService = messageService;
        this.userAuth = userAuth;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication authentication = userAuth.authenticateUser(loginRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return tokenGenerator.generateToken();
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }
}
