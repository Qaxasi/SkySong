package com.mycompany.SkySong.logout;

import com.mycompany.SkySong.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {

    private final SessionDeletion sessionDeletion;

    public LogoutController(SessionDeletion sessionDeletion) {
        this.sessionDeletion = sessionDeletion;
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("session_id") String token) {
        sessionDeletion.deleteSession(token);
        return ResponseEntity.ok(new ApiResponse("Logged out successfully."));
    }
}