package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.mycompany.SkySong.infrastructure.web.ErrorView;
import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.identity.infrastructure.config.token.RefreshTokenProperties;
import com.mycompany.SkySong.identity.authentication.application.login.dto.LoginCredentials;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.infrastructure.web.SuccessResponse;
import com.mycompany.SkySong.identity.authentication.application.login.service.UserAuthenticator;
import com.mycompany.SkySong.infrastructure.web.BaseResponse;
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

    public LoginController(final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils,
                           final RefreshTokenProperties refreshTokenProperties,
                           final HttpErrorMapper errorMapper) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
        this.refreshTokenProperties = refreshTokenProperties;
        this.errorMapper = errorMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody final LoginRequest request) {
        return authenticator.login(new LoginCredentials(request.username(), request.password()))
                .fold(
                        error -> errorMapper.from(ErrorView.fromResult(error)),

                        authenticationTokens -> {
                            final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
                                    refreshTokenProperties.cookie(), authenticationTokens.refreshToken().value());

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                    .headers(h -> h.setBearerAuth(authenticationTokens.accessToken().value()))
                                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                                    .body(new SuccessResponse("Logged successfully."));
                        });
    }
}
