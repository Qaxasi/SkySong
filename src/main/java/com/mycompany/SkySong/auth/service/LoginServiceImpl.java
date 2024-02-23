package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoginServiceImpl implements LoginService {

    private final SessionCreation sessionCreation;
    private final ApplicationMessageService messageService;
    private final UserDAO userDAO;
    private final UserAuthentication authentication;

    public LoginServiceImpl(SessionCreation sessionCreation,
                            ApplicationMessageService messageService,
                            UserDAO userDAO,
                            UserAuthentication authentication) {
        this.sessionCreation = sessionCreation;
        this.messageService = messageService;
        this.userDAO = userDAO;
        this.authentication = authentication;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication auth = authentication.authenticateUser(loginRequest);

            User user = userDAO.findByUsername(auth.getName())
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User not found with username: " + auth.getName()));

            return sessionCreation.createSession(user.getId());
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }
}
