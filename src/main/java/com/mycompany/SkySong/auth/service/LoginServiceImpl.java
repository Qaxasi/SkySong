package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final LoginManager loginManager;
    private final ApplicationMessageService messageService;


    public LoginServiceImpl(ApplicationMessageService messageService,
                            LoginManager loginManager) {
        this.loginManager = loginManager;
        this.messageService = messageService;

    }

    @Override
    public String login(LoginRequest request) {
        try {
            Authentication auth = userAuth.authenticateUser(request);
            User user = userRetrieval.findByAuthUsername(auth.getName());
            return sessionCreation.createSession(user.getId());
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", request.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }
}
