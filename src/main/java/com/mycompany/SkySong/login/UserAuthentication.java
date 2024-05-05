package com.mycompany.SkySong.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserAuthentication {

    private final AuthenticationManager authManager;

    public UserAuthentication(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    public Authentication authenticateUser(LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.usernameOrEmail(), request.password()));
            SecurityContextHolder.getContext().setAuthentication(auth);

            return auth;
        } catch (BadCredentialsException e) {
            log.error("Error during login for user: {}", request.usernameOrEmail(), e);
            throw new BadCredentialsException("Logout failed due to an internal error.");
        }
    }
}
