package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationImpl implements UserAuthentication {

    private final AuthenticationManager authenticationManager;
    private final ApplicationMessageService message;

    public UserAuthenticationImpl(AuthenticationManager authenticationManager, ApplicationMessageService message) {
        this.authenticationManager = authenticationManager;
        this.message = message;
    }

    @Override
    public Authentication authenticateUser(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usernameOrEmail(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        return auth;
    }
}
