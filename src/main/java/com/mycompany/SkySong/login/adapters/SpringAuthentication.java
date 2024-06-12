package com.mycompany.SkySong.login.adapters;

import com.mycompany.SkySong.login.domain.ports.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
class SpringAuthentication implements UserAuthentication {

    private final AuthenticationManager authManager;

    SpringAuthentication(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public String authenticateUser(String usernameOrEmail, String password) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth.getName();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("    Login failed due to invalid credentials.");
        }
    }
}