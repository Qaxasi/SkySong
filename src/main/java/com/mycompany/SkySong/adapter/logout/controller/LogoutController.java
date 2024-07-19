package com.mycompany.SkySong.adapter.logout.controller;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.domain.logout.ports.LogoutSessionRepository;
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
        sessionRepository.deleteById(token);
        return ResponseEntity.ok(new ApiResponse("Logged out successfully."));
    }
}