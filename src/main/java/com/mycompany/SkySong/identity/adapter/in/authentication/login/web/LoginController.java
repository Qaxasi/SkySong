package com.mycompany.SkySong.identity.adapter.in.authentication.login.web;

import com.mycompany.SkySong.config.jwt.JwtAccessTokenProperties;
import com.mycompany.SkySong.config.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.identity.application.authentication.login.dto.LoginInput;
import com.mycompany.SkySong.shared.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.identity.application.authentication.shared.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.application.authentication.login.service.UserAuthenticator;
import com.mycompany.SkySong.shared.response.BaseResponse;
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
    private final JwtAccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    public LoginController(final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils,
                           final JwtAccessTokenProperties accessTokenProperties,
                           final RefreshTokenProperties refreshTokenProperties) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody final LoginRequest request) {
        final Result<AuthenticationTokens> result =
                authenticator.login(new LoginInput(request.username(), request.password()));

        if (result.isFailure()) {
            return ResponseEntity
                    .status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        final ResponseCookie accessTokenCookie = cookieUtils.generateCookie(
                accessTokenProperties.cookie(), result.data().accessToken().value());

        final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
                refreshTokenProperties.cookie(), result.data().refreshToken().value());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new SuccessResponse("Logged successfully."));
    }
}
