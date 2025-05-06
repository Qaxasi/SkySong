package com.mycompany.SkySong.adapter.user.login.controller;

import com.mycompany.SkySong.application.user.login.dto.LoginInput;
import com.mycompany.SkySong.adapter.shared.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.ApiResponse;
import com.mycompany.SkySong.adapter.user.login.dto.LoginRequest;
import com.mycompany.SkySong.application.user.login.dto.AuthenticationTokens;
import com.mycompany.SkySong.application.user.login.usecase.UserAuthenticator;
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

    public LoginController(final UserAuthenticator authenticator,
                           final CookieUtils cookieUtils) {
        this.authenticator = authenticator;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody final LoginRequest request) {
        final Result<AuthenticationTokens> result =
                authenticator.login(new LoginInput(request.usernameOrEmail(), request.password()));

        if (result.isFailure()) {
            return ResponseEntity
                    .status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        final ResponseCookie accessTokenCookie = cookieUtils.generateCookie(
                "accessToken", result.data().accessToken(), "/api", 600);

        final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
                "refreshToken", result.data().refreshToken(), "/api/v1/auth/refresh-token", 86400);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new ApiResponse("Logged successfully."));
    }
}
