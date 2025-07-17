package com.mycompany.SkySong.identity.adapter.in.authentication.refresh.web;

import com.mycompany.SkySong.config.jwt.JwtAccessTokenProperties;
import com.mycompany.SkySong.config.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.identity.application.authentication.refresh.service.AccessTokenRefresher;
import com.mycompany.SkySong.identity.application.authentication.shared.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.domain.RefreshToken;
import com.mycompany.SkySong.shared.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.shared.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/token")
public class AccessTokenRefresherController {

    private final CookieUtils cookieUtils;
    private final AccessTokenRefresher accessTokenRefresher;
    private final JwtAccessTokenProperties accessTokenProperties;
    private final RefreshTokenProperties refreshTokenProperties;

    public AccessTokenRefresherController(final CookieUtils cookieUtils,
                                          final AccessTokenRefresher accessTokenRefresher,
                                          final JwtAccessTokenProperties accessTokenProperties,
                                          final RefreshTokenProperties refreshTokenProperties) {
        this.cookieUtils = cookieUtils;
        this.accessTokenRefresher = accessTokenRefresher;
        this.accessTokenProperties = accessTokenProperties;
        this.refreshTokenProperties = refreshTokenProperties;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshToken(final HttpServletRequest request) {
        final String refreshToken = cookieUtils.getCookieValue(request, "refreshToken");

        final Result<AuthenticationTokens> result =
                accessTokenRefresher.refreshAccessToken(new RefreshToken(refreshToken));

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
                .body(new SuccessResponse("Your session has been successfully extended."));
    }
}
