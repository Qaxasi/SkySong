package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
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
    private final CredentialsAuthentication authentication;
    private final UserDAO userDAO;

    public LoginServiceImpl(SessionCreation sessionCreation, ApplicationMessageService messageService,
                            CredentialsAuthentication authentication, UserDAO userDAO) {
        this.sessionCreation = sessionCreation;
        this.messageService = messageService;
        this.authentication = authentication;
        this.userDAO = userDAO;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication auth = authentication.authenticateUser(loginRequest);
            SecurityContextHolder.getContext().setAuthentication(auth);

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
