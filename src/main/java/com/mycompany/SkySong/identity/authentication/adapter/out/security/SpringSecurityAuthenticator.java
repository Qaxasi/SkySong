package com.mycompany.SkySong.identity.authentication.adapter.out.security;

import com.mycompany.SkySong.infrastructure.security.user.CustomUserDetails;
import com.mycompany.SkySong.identity.authentication.application.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.identity.authentication.application.login.port.Authenticator;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    public Result<AuthenticatedUser> authenticate(final String username, final String password) {
        try {
            final Authentication authentication = authManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password));

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            final List<String> roles = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return Result.success(new AuthenticatedUser(userDetails.id(), userDetails.getUsername(), roles));
        } catch (BadCredentialsException ex) {
            return Result.failure("Invalid username or password", ErrorType.AUTHENTICATION_FAILED);
        } catch (LockedException | DisabledException ex) {
            return Result.failure("Account is not allowed to sign in", ErrorType.ACCOUNT_RESTRICTED);
        } catch (AuthenticationException ex) {
            return Result.failure("Authentication failed", ErrorType.AUTHENTICATION_FAILED);
        }
    }
}
