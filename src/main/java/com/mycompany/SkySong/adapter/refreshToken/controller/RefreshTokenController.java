package com.mycompany.SkySong.adapter.refreshToken.controller;

import com.mycompany.SkySong.shared.error.ErrorResponse;
import com.mycompany.SkySong.adapter.refreshToken.handler.RefreshTokenHandler;
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
@RequestMapping("/api/v1/auth")
public class RefreshTokenController {

    private final CookieUtils cookieUtils;
    private final RefreshTokenHandler tokenHandler;

    public RefreshTokenController(CookieUtils cookieUtils,
                                  RefreshTokenHandler tokenHandler) {
        this.cookieUtils = cookieUtils;
        this.tokenHandler = tokenHandler;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        String refreshToken = cookieUtils.getJwtFromCookies(request, "refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(
                            "Session renewal failed: please log in again.",
                            ErrorType.FORBIDDEN.name(),
                            ErrorType.FORBIDDEN.getHttpStatus().value()
                    ));
        }

        String newJwtToken = tokenHandler.generateAccessTokenFromRefreshToken(refreshToken);

        ResponseCookie jwtCookie = cookieUtils.generateCookie("jwtToken", newJwtToken, "/api", 600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new ApiResponse("Your session has been successfully extended."));
    }
}
