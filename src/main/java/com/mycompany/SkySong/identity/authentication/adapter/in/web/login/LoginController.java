package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.mycompany.SkySong.infrastructure.web.contract.ResponsePayload;
import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.identity.infrastructure.authentication.config.token.RefreshTokenProperties;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.identity.authentication.application.login.service.UserAuthenticator;
import com.mycompany.SkySong.shared.result.Failure;
import jakarta.validation.Valid;
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
    private final RefreshTokenProperties refreshTokenProperties;
    private final HttpErrorMapper errorMapper;
    private final RawPasswordGuard passwordGuard;

    public LoginController(final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils,
                           final RefreshTokenProperties refreshTokenProperties,
                           final HttpErrorMapper errorMapper,
                           final RawPasswordGuard passwordGuard) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
        this.refreshTokenProperties = refreshTokenProperties;
        this.errorMapper = errorMapper;
        this.passwordGuard = passwordGuard;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload<AuthResponse>> login(@Valid @RequestBody final LoginRequest request) {
        return Username.of(request.username())
                .flatMap(username ->
                        passwordGuard.useAndZeroize(request.password(),
                        pwd -> authenticator.login(username, pwd)))
                .fold(
                        this::maskLoginFailure,

                        accessGrant -> {
                            final ResponseCookie cookie = cookieUtils.generateCookie(
                                    refreshTokenProperties.cookie(),
                                    accessGrant.refreshToken().value(),
                                    accessGrant.refreshTokenTtl());

                            final AuthResponse response = new AuthResponse(
                                    accessGrant.accessToken().value(),
                                    accessGrant.accessTokenTtl().getSeconds());

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                    .body(ResponsePayload.ok(response));
                        });
    }

    private ResponseEntity<ResponsePayload<AuthResponse>> maskLoginFailure(final Failure<?> f) {
        final HttpStatus status = f.errorType().getHttpStatus();

        if (status.is4xxClientError()) {
            return switch (f.errorType()) {
                case ACCOUNT_LOCKED, ACCOUNT_DISABLED ->
                        errorMapper.failure("Login not allowed", f.errorType());
                default ->
                        errorMapper.failure("Invalid login credentials", f.errorType());
            };
        }
        return errorMapper.failure("Service temporarily unavailable", f.errorType());
    }
}
