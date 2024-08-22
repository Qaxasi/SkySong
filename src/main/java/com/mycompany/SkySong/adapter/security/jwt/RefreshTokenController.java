package com.mycompany.SkySong.adapter.security.jwt;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
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
    private final RefreshTokenHandler refreshTokenHandler;

    public RefreshTokenController(CookieUtils cookieUtils,
                                  RefreshTokenHandler refreshTokenHandler) {
        this.cookieUtils = cookieUtils;
        this.refreshTokenHandler = refreshTokenHandler;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = cookieUtils.getJwtFromCookies(request, "refreshToken");

        if (refreshToken != null && validateRefreshToken(refreshToken)) {
            String newJwtToken = generateAccessTokenFromRefreshToken(refreshToken);
            ResponseCookie jwtCookie = cookieUtils.generateCookie("jwtToken", newJwtToken, "/api");

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ApiResponse("Your session has been successfully extended."));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse("Session renewal failed: please log in again."));
    }
}
