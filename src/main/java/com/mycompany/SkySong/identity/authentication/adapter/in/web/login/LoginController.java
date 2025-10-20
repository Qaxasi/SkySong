package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.identity.infrastructure.authentication.config.token.RefreshTokenProperties;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.identity.authentication.application.login.service.UserAuthenticator;
import com.mycompany.SkySong.infrastructure.web.BaseResponse;
import com.mycompany.SkySong.infrastructure.web.SuccessResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
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
    private final RawPasswordScope passwordScope;

    public LoginController(final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils,
                           final RefreshTokenProperties refreshTokenProperties,
                           final HttpErrorMapper errorMapper,
                           final RawPasswordScope passwordScope) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
        this.refreshTokenProperties = refreshTokenProperties;
        this.errorMapper = errorMapper;
        this.passwordScope = passwordScope;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody final LoginRequest request) {
        final Result<Username> usernameRes = Username.of(request.username());
        if (usernameRes.isFailure()) {
            return errorMapper.failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
        }

        return passwordScope.useAndZeroize(request.password(), password ->
                authenticator.login(usernameRes.get(), password))
                .fold(
                        error -> {
                            if (error.errorType() == ErrorType.VALIDATION_ERROR) {
                                return errorMapper.failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
                            }
                            return errorMapper.from(error);
                        },
                        authTokens -> {
                            final ResponseCookie cookie = cookieUtils.generateCookie(
                                    refreshTokenProperties.cookie(), authTokens.refreshToken().value());

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                    .headers(h -> h.setBearerAuth(authTokens.accessToken().value()))
                                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                    .body(new SuccessResponse("Logged successfully"));
                        });
    }
}
