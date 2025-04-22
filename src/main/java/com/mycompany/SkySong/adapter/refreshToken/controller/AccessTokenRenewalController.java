package com.mycompany.SkySong.adapter.refreshToken.controller;

import com.mycompany.SkySong.shared.error.ErrorResponse;
import com.mycompany.SkySong.adapter.refreshToken.handler.AccessTokenRenewalService;
import com.mycompany.SkySong.adapter.utils.CookieUtils;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
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
public class AccessTokenRenewalController {

    private final CookieUtils cookieUtils;
    private final AccessTokenRenewalService renewalService;

    public AccessTokenRenewalController(CookieUtils cookieUtils,
                                        AccessTokenRenewalService renewalService) {
        this.cookieUtils = cookieUtils;
        this.renewalService = renewalService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshToken = cookieUtils.getJwtFromCookies(request, "refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "Session renewal failed: please log in again.",
                            ErrorType.UNAUTHORIZED.name(),
                            ErrorType.UNAUTHORIZED.getHttpStatus().value()
                    ));
        }

        String newAccessToken = renewalService.generateAccessTokenFromRefreshToken(refreshToken);

        ResponseCookie cookie = cookieUtils.generateCookie(
                "jwtToken",
                newAccessToken,
                "/api",
                600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse("Your session has been successfully extended."));
    }
}
