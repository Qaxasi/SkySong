package com.mycompany.skysong.identity.adapter.out.springsecurity;

import com.mycompany.skysong.identity.application.authentication.model.AuthenticatedIdentity;
import com.mycompany.skysong.identity.application.authentication.port.Authenticator;
import com.mycompany.skysong.identity.domain.RawPassword;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.*;

@Component
public class SpringSecurityAuthenticator implements Authenticator {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityAuthenticator.class);
    private final AuthenticationManager authManager;

    public SpringSecurityAuthenticator(final AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Result<AuthenticatedIdentity> authenticate(final Username username,
                                                      final RawPassword password) {
        try {
            final Authentication authentication = authManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            username.asString(),
                            password.asCharSequenceView()));

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return UserId.fromStored(userDetails.getId())
                    .map(AuthenticatedIdentity::new);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            return Result.failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
        } catch (AuthenticationServiceException ex) {
            log.error("authentication service error {}",
                    kv("op", "user.authentication"),
                    ex);
            return Result.failure("Authentication failed", ErrorType.AUTHENTICATION_SERVICE_ERROR);
        } catch (AuthenticationException ex) {
            log.error("unexpected authentication error {}",
                    kv("op", "user.authentication"),
                    ex);
            return Result.failure("Authentication failed", ErrorType.AUTHENTICATION_FAILED);
        }
    }
}
