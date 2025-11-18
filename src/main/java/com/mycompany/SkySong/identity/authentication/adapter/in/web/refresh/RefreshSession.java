package com.mycompany.SkySong.identity.authentication.adapter.in.web.refresh;

import com.mycompany.SkySong.identity.authentication.adapter.in.web.login.AuthResponse;
import com.mycompany.SkySong.identity.authentication.application.service.SessionRefresher;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.infrastructure.web.contract.ResponsePayload;
import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// jaki path jakie nazwy dla klasy metody ?
@RestController
@RequestMapping("/api/v1/auth/token")
public class RefreshSession {
    private final SessionRefresher sessionRefresher;
    private final CookieUtils cookieUtils;
    private final HttpErrorMapper errorMapper;
    private final CookieProperties refreshTokenCookieProperties;
    private final CookieProperties userTagCookieProperties;

    public RefreshSession(final SessionRefresher sessionRefresher,
                                        final CookieUtils cookieUtils,
                                        final HttpErrorMapper errorMapper,
                                        @Qualifier("refreshTokenCookieProperties")
                                        final CookieProperties refreshTokenCookieProperties,
                                        @Qualifier("userTagCookieProperties")
                                        final CookieProperties userTagCookieProperties) {
        this.sessionRefresher = sessionRefresher;
        this.cookieUtils = cookieUtils;
        this.errorMapper = errorMapper;
        this.refreshTokenCookieProperties = refreshTokenCookieProperties;
        this.userTagCookieProperties = userTagCookieProperties;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponsePayload<AuthResponse>> refreshTokens(final HttpServletRequest request) {
        return cookieUtils.getCookieValue(request, refreshTokenProperties.cookie().name())
                .flatMap(refreshToken -> tokenPairRefresher.refresh(refreshToken))
                .fold(
                        error -> errorMapper.from(ErrorView.fromResult(error)),

                        tokens -> {
                            final ResponseCookie accessTokenCookie = cookieUtils.generateCookie(
                                    accessTokenProperties.cookie(), tokens.accessToken().value());

                            final ResponseCookie refreshTokenCookie = cookieUtils.generateCookie(
                                    refreshTokenProperties.cookie(), tokens.refreshToken().value());

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                    .body(new SuccessResponse("Your session has been successfully extended."));
                        });
    }
}
