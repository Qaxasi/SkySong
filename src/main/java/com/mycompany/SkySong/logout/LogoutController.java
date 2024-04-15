package com.mycompany.SkySong.logout;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {

    private final ApplicationMessageLoader applicationMessageLoader;
    private final SessionDeletion sessionDeletion;

    public LogoutController(ApplicationMessageLoader applicationMessageLoader, SessionDeletion sessionDeletion) {
        this.applicationMessageLoader = applicationMessageLoader;
        this.sessionDeletion = sessionDeletion;
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("session_id") String token) {
        sessionDeletion.deleteSession(token);
        return ResponseEntity.ok(new ApiResponse(applicationMessageLoader.getMessage("logout.success")));
    }
}