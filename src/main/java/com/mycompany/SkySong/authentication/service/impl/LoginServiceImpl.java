package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.dto.LoginRequest;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationMessageService messageService;
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider,
                            ApplicationMessageService messageService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.messageService = messageService;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationUser(loginRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.generateToken(authentication);
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw new BadCredentialsException(messageService.getMessage("login.failure"));
        }
    }

    private Authentication authenticationUser(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail(), loginRequest.password()));
    }
}
