package com.mycompany.skysong.identity.adapter.in.web.authentication;

import com.mycompany.skysong.identity.adapter.in.web.cookie.CookieUtils;
import com.mycompany.skysong.identity.application.model.AccessGrant;
import com.mycompany.skysong.identity.application.service.SessionRefresher;
import com.mycompany.skysong.identity.config.CookieProperties;
import com.mycompany.skysong.core.result.Failure;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.UserTag;
import com.mycompany.skysong.web.error.ErrorTypeToHttpStatusMapper;
import com.mycompany.skysong.web.response.ResponsePayload;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final CookieProperties refreshTokenCookieProperties;
    private final CookieProperties userTagCookieProperties;

    public RefreshSessionController(final SessionRefresher sessionRefresher,
                                    final CookieUtils cookieUtils,
                                    @Qualifier("refreshTokenCookieProperties")
                                    final CookieProperties refreshTokenCookieProperties,
                                    @Qualifier("userTagCookieProperties")
                                    final CookieProperties userTagCookieProperties) {
        this.sessionRefresher = sessionRefresher;
        this.cookieUtils = cookieUtils;
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
                this::mapRefreshSessionFailure,

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

    private ResponseEntity<ResponsePayload<AuthResponse>> mapRefreshSessionFailure(final Failure<AccessGrant> failure) {
        final HttpStatus status = ErrorTypeToHttpStatusMapper.toHttpStatus(failure.errorType());

        if (status.is4xxClientError()) {
            return RefreshSessionErrorResponses.unauthorizedErrorResponse();
        }
        return RefreshSessionErrorResponses.internalErrorResponse();
    }
}



