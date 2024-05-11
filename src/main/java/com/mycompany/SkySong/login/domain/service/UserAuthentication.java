package com.mycompany.SkySong.login;

import com.mycompany.SkySong.login.application.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

class UserAuthentication {
    private final AuthenticationManager authManager;

    UserAuthentication(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    Authentication authenticateUser(LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.usernameOrEmail(), request.password()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Logout failed due to an internal error.");
        }
    }
}
