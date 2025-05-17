package com.mycompany.SkySong.adapter.user.token.refresh.controller;

import com.mycompany.SkySong.application.user.token.refresh.usecase.AccessTokenRefresher;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.ErrorResponse;
import com.mycompany.SkySong.adapter.shared.cookie.CookieUtils;
import com.mycompany.SkySong.shared.response.ApiResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;
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
public class AccessTokenRefresherController {

    private final CookieUtils cookieUtils;
    private final AccessTokenRefresher accessTokenRefresher;

    public AccessTokenRefresherController(final CookieUtils cookieUtils,
                                          final AccessTokenRefresher accessTokenRefresher) {
        this.cookieUtils = cookieUtils;
        this.accessTokenRefresher = accessTokenRefresher;
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse> refreshToken(final HttpServletRequest request) {
        final String refreshToken = cookieUtils.getCookieValue(request, "refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "Session renewal failed: please log in again.",
                            ErrorType.UNAUTHORIZED.name(),
                            ErrorType.UNAUTHORIZED.getHttpStatus().value()
                    ));
        }

        final Result<String> result = accessTokenRefresher.refreshAccessToken(refreshToken);

        if (result.isFailure()) {
            return ResponseEntity
                    .status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        final ResponseCookie cookie = cookieUtils.generateAccessTokenCookie(result.data());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Your session has been successfully extended."));
    }
}
