package com.mycompany.skysong.identity.adapter.in.web.authentication;

import com.mycompany.skysong.identity.adapter.in.web.cookie.CookieUtils;
import com.mycompany.skysong.identity.application.model.AccessGrant;
import com.mycompany.skysong.identity.application.service.UserAuthenticator;
import com.mycompany.skysong.identity.config.CookieProperties;
import com.mycompany.skysong.core.result.Failure;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.web.error.ErrorTypeToHttpStatusMapper;
import com.mycompany.skysong.web.response.ResponsePayload;
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
    private final RawPasswordGuard passwordGuard;

    public LoginController(@Qualifier("refreshTokenCookieProperties")
                           final CookieProperties refreshTokenCookieProps,
                           @Qualifier("userTagCookieProperties")
                           final CookieProperties userTagCookieProps,
                           final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils,
                           final RawPasswordGuard passwordGuard) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
        this.refreshTokenCookieProps = refreshTokenCookieProps;
        this.userTagCookieProps = userTagCookieProps;
        this.passwordGuard = passwordGuard;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload<AuthResponse>> login(@Valid @RequestBody final LoginRequest request) {
        return Username.fromInput(request.username())
                .flatMap(username ->
                        passwordGuard.useAndZeroize(request.password(),
                        password -> authenticator.login(username, password)))
                .fold(
                        this::mapLoginFailure,

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

    private ResponseEntity<ResponsePayload<AuthResponse>> mapLoginFailure(final Failure<AccessGrant> failure) {
        final HttpStatus status = ErrorTypeToHttpStatusMapper.toHttpStatus(failure.errorType());

        if (status.is4xxClientError()) {
            return LoginErrorResponses.unauthorizedErrorResponse();
        }
        return LoginErrorResponses.internalErrorResponse();
    }
}
