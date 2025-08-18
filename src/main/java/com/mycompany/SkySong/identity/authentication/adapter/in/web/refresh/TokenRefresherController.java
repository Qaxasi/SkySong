package com.mycompany.SkySong.identity.authentication.adapter.in.web.refresh;

import com.mycompany.SkySong.infrastructure.security.jwt.JwtAccessTokenProperties;
import com.mycompany.SkySong.infrastructure.security.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.identity.authentication.application.refresh.service.TokenRefresher;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/token")
public class TokenRefresherController {

    private final CookieUtils cookieUtils;
    private final TokenRefresher accessTokenRefresher;
    private final JwtAccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    public TokenRefresherController(final CookieUtils cookieUtils,
                                    final TokenRefresher accessTokenRefresher,
                                    final JwtAccessTokenProperties accessTokenProperties,
                                    final RefreshTokenProperties refreshTokenProperties) {
        this.cookieUtils = cookieUtils;
        this.accessTokenRefresher = accessTokenRefresher;
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshTokens(final HttpServletRequest request) {
        return cookieUtils.getCookieValue(request, refreshTokenProperties.cookie().name())
                .fold(error -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(error.toErrorResponse()),

                        refreshToken -> accessTokenRefresher.refresh(refreshToken)
                                .fold(error ->
                                        ResponseEntity
                                                .status(error.errorType().getHttpStatus())
                                                .body(error.toErrorResponse()),

                                        data -> {
                                            final ResponseCookie accessTokenCookie = cookieUtils.generateCookie(
                                                    accessTokenProperties.cookie(), data.accessToken().value());

                                            final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
                                                    refreshTokenProperties.cookie(), data.refreshToken().value());

                                            return ResponseEntity.ok()
                                                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                                                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                                    .body(new SuccessResponse("Your session has been successfully extended."));
                                        }));
    }
}
