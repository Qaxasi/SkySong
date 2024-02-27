package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final LoginSessionManager loginSessionManager;
    private final ApplicationMessageService messageService;


    public LoginServiceImpl(ApplicationMessageService messageService,
                            LoginSessionManager loginSessionManager) {
        this.loginSessionManager = loginSessionManager;
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
