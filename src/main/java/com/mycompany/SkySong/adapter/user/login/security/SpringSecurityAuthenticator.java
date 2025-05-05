package com.mycompany.SkySong.adapter.user.login.security;

import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.adapter.user.login.exception.InvalidCredentialsException;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.application.user.login.model.AuthenticatedUser;
import com.mycompany.SkySong.application.user.login.port.Authenticator;
import com.mycompany.SkySong.shared.error.ErrorType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpringSecurityAuthenticator implements Authenticator {
    private final ApplicationLogger logger;
    private final AuthenticationManager authManager;

    public SpringSecurityAuthenticator(final ApplicationLogger logger,
                                       final AuthenticationManager authManager) {
        this.logger = logger;
        this.authManager = authManager;
    }

    @Override
    public AuthenticatedUser authenticate(String usernameOrEmail, String password) {
        try {
            final Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            final List<String> roles = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            logger.info("User authenticated successfully", ApplicationLogger.Context.of(Map.of(
                    "userId", userDetails.id(),
                    "username", userDetails.getUsername(),
                    "roles", roles
            )));

            return new AuthenticatedUser(userDetails.id(), userDetails.getUsername(), roles);
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user/email",
                    ApplicationLogger.Context.of("usernameOrEmail", usernameOrEmail));
            throw new InvalidCredentialsException(
                    "Invalid username or password.",
                    ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
    }
}
