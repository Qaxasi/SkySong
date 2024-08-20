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
@RequestMapping("/api/v1/users")
public class RefreshTokenController {
    private final JwtTokenManager tokenManager;
    private final CookieUtils cookieUtils;

    public RefreshTokenController(JwtTokenManager tokenManager,
                                  CookieUtils cookieUtils) {
        this.tokenManager = tokenManager;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = getJwtFromCookies(request, "refreshToken");

        if (refreshToken != null && validateRefreshToken(refreshToken)) {
            String newJwtToken = generateAccessTokenFromRefreshToken(refreshToken);
            ResponseCookie jwtCookie = generateCookie("jwtToken", newJwtToken, "/api");

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ApiResponse("Your session has been successfully extended."));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("Session renewal failed: please log in again."));
    }
}
