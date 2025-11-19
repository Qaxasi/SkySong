package com.mycompany.SkySong.identity.authentication.adapter.out.auth.springsecurity;

import com.mycompany.SkySong.identity.authentication.domain.RawPassword;
import com.mycompany.SkySong.identity.authentication.domain.UserId;
import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.identity.infrastructure.authentication.security.springboot.CustomUserDetails;
import com.mycompany.SkySong.identity.authentication.application.dto.AuthenticatedIdentity;
import com.mycompany.SkySong.identity.authentication.application.port.Authenticator;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
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
        try (password) {
            final Authentication authentication = authManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            username.asString(),
                            password.charSequenceView()));

            final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return UserId.fromStored(userDetails.getId())
                    .map(AuthenticatedIdentity::new);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            return Result.failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
        } catch (LockedException ex ) {
            return Result.failure("Account is locked", ErrorType.ACCOUNT_LOCKED);
        } catch (DisabledException ex) {
            return Result.failure("Account is disabled", ErrorType.ACCOUNT_DISABLED);
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
