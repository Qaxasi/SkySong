package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.exception.ServiceFailureException;
import com.mycompany.SkySong.authentication.service.CookieService;
import com.mycompany.SkySong.authentication.service.impl.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {
    private final CookieService cookieService;
    public LogoutController(CookieService cookieService) {
        this.cookieService = cookieService;
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            CookieUtils.deleteCookie(request, response, "auth_token");
            return ResponseEntity.ok("User logged out successfully");
        } catch (RuntimeException e) {
            throw new ServiceFailureException("Logout failed due to an internal error");
        }
    }
}
