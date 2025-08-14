package com.mycompany.SkySong.identity.authentication.adapter.out.x;

import com.mycompany.SkySong.infrastructure.security.user.CustomUserDetails;
import com.mycompany.SkySong.identity.authentication.application.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.identity.authentication.application.login.port.Authenticator;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;
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
    private final ApplicationLogger logger;

    public SpringSecurityAuthenticator(final AuthenticationManager authManager,
                                       final ApplicationLogger logger) {
        this.authManager = authManager;
        this.logger = logger;
    }

    @Override
    public Result<AuthenticatedUser> authenticate(final String username, final String password) {
        try {
            final Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            final List<String> roles = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return Result.success(new AuthenticatedUser(userDetails.id(), userDetails.getUsername(), roles));
        } catch (BadCredentialsException e) {
            logger.warn("Invalid login attempt");
            return Result.failure("Invalid username or password", ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
    }
}
