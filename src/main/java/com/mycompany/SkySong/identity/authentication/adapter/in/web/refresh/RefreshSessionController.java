package com.mycompany.SkySong.identity.authentication.adapter.in.web.refresh;

import com.mycompany.SkySong.identity.authentication.adapter.in.web.login.AuthResponse;
import com.mycompany.SkySong.identity.authentication.application.service.SessionRefresher;
import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.UserTag;
import com.mycompany.SkySong.infrastructure.cookie.CookieUtils;
import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.infrastructure.web.contract.ResponsePayload;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class RefreshSessionController {
    private final SessionRefresher sessionRefresher;
    private final CookieUtils cookieUtils;
    private final HttpErrorMapper errorMapper;
    private final CookieProperties refreshTokenCookieProperties;
    private final CookieProperties userTagCookieProperties;

    public RefreshSessionController(final SessionRefresher sessionRefresher,
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
    public ResponseEntity<ResponsePayload<AuthResponse>> refresh(final HttpServletRequest request) {
        return Result.combineM(
                cookieUtils.getCookieValue(request, refreshTokenCookieProperties)
                        .flatMap(RefreshToken::fromInput),
                cookieUtils.getCookieValue(request, userTagCookieProperties)
                        .flatMap(UserTag::fromInput),

                sessionRefresher::refresh
        ).fold(
                errorMapper::mapRefreshSessionFailure,

                accessGrant -> {
                    final ResponseCookie rtCookie = cookieUtils.generateCookie(
                            refreshTokenCookieProperties,
                            accessGrant.refreshToken().value(),
                            accessGrant.sessionTtl());

                    final ResponseCookie utCookie = cookieUtils.generateCookie(
                            userTagCookieProperties,
                            accessGrant.userTag().asString(),
                            accessGrant.sessionTtl());

                    final AuthResponse response = new AuthResponse(
                            accessGrant.accessToken().value(),
                            accessGrant.accessTokenExpiresInSec());

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, rtCookie.toString())
                            .header(HttpHeaders.SET_COOKIE, utCookie.toString())
                            .header(HttpHeaders.CACHE_CONTROL, "no-store")
                            .body(ResponsePayload.ok(response));
                });
    }
}



