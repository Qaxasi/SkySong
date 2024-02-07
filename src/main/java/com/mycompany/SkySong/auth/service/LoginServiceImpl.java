package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.SecureTokenGenerator;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final SecureTokenGenerator tokenGenerator;
    private final ApplicationMessageService messageService;
    private final UserAuthenticationService userAuth;

    public LoginServiceImpl(SecureTokenGenerator tokenGenerator, ApplicationMessageService messageService,
                            UserAuthenticationService userAuth) {
        this.tokenGenerator = tokenGenerator;
        this.messageService = messageService;
        this.userAuth = userAuth;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication authentication = userAuth.authenticateUser(loginRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return generator.generateToken(authentication);
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }
}
