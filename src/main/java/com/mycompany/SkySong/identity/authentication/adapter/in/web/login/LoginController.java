package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.mycompany.SkySong.identity.authentication.application.service.UserAuthenticator;
import com.mycompany.SkySong.infrastructure.web.contract.ResponsePayload;
import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Failure;
import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {
    private final UserAuthenticator authenticator;
    private final CookieUtils cookieUtils;
    private final CookieProperties refreshTokenCookieProps;
    private final CookieProperties userTagCookieProps;
    private final HttpErrorMapper errorMapper;
    private final RawPasswordGuard passwordGuard;

    public LoginController(@Qualifier("refreshTokenCookieProperties")
                           final CookieProperties refreshTokenCookieProps,
                           @Qualifier("userTagCookieProperties")
                           final CookieProperties userTagCookieProps,
                           final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils,
                           final HttpErrorMapper errorMapper,
                           final RawPasswordGuard passwordGuard) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
        this.refreshTokenCookieProps = refreshTokenCookieProps;
        this.userTagCookieProps = userTagCookieProps;
        this.errorMapper = errorMapper;
        this.passwordGuard = passwordGuard;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload<AuthResponse>> login(@Valid @RequestBody final LoginRequest request) {
        return Username.fromInput(request.username())
                .flatMap(username ->
                        passwordGuard.useAndZeroize(request.password(),
                        pwd -> authenticator.login(username, pwd)))
                .fold(
                        this::maskLoginFailure,

                        accessGrant -> {
                            final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
                                    refreshTokenCookieProps,
                                    accessGrant.refreshToken().value(),
                                    accessGrant.sessionTtl());

                            final ResponseCookie userTagCookie = cookieUtils.generateCookie(
                                    userTagCookieProps,
                                    accessGrant.userTag().asString(),
                                    accessGrant.sessionTtl());

                            final AuthResponse response = new AuthResponse(
                                    accessGrant.accessToken().value(),
                                    accessGrant.accessTokenExpiresInSec());

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                    .header(HttpHeaders.SET_COOKIE, userTagCookie.toString())
                                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                    .body(ResponsePayload.ok(response));
                        });
    }

    private ResponseEntity<ResponsePayload<AuthResponse>> maskLoginFailure(final Failure<?> f) {
        final HttpStatus status = f.errorType().getHttpStatus();

        if (status.is4xxClientError()) {
            return errorMapper.failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
        return errorMapper.failure("Internal service error", f.errorType());
    }
}
