package com.mycompany.SkySong.login.adapters;

import com.mycompany.SkySong.login.domain.ports.AuthenticationPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
class AuthenticationAdapter implements AuthenticationPort {

    private final AuthenticationManager authManager;

    AuthenticationAdapter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public String authenticateUser(String usernameOrEmail, String password) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth.getName();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Logout failed due to an internal error.");
        }
    }
}
