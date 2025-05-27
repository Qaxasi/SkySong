package com.mycompany.SkySong.adapter.user.authentication.login.security;

import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.application.user.authentication.login.exception.InvalidCredentialsException;
import com.mycompany.SkySong.application.user.authentication.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.application.user.authentication.login.ports.Authenticator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringSecurityAuthenticator implements Authenticator {
    private final AuthenticationManager authManager;

    public SpringSecurityAuthenticator(final AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public AuthenticatedUser authenticate(final String usernameOrEmail, final String password) {
        try {
            final Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            final List<String> roles = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new AuthenticatedUser(userDetails.id(), userDetails.getUsername(), roles);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }
}
