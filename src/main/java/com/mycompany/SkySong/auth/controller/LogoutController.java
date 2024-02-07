package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.SessionDeletion;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {

    private final ApplicationMessageService messageService;
    private final SessionDeletion sessionDeletion;

    public LogoutController(ApplicationMessageService messageService, SessionDeletion sessionDeletion) {
        this.messageService = messageService;
        this.sessionDeletion = sessionDeletion;
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("session_id") String token) {
        sessionDeletion.deleteSession(token);
        return ResponseEntity.ok(new ApiResponse(messageService.getMessage("logout.success")));
    }
}