package com.mycompany.SkySong.logout.application.controller;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.logout.domain.ports.LogoutSessionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {

    private final LogoutSessionRepository sessionRepository;
    public LogoutController(LogoutSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@CookieValue("session_id") String token) {
        logoutService.deleteUserSession(token);
        return ResponseEntity.ok(new ApiResponse("Logged out successfully."));
    }
}