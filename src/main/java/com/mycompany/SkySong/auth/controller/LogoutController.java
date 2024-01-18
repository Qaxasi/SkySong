package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.security.CookieDeleter;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {
    private final CookieDeleter cookieDeleter;
    private final ApplicationMessageService messageService;
    public LogoutController(CookieDeleter cookieDeleter, ApplicationMessageService messageService) {
        this.cookieDeleter = cookieDeleter;
        this.messageService = messageService;
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        cookieDeleter.deleteCookie(request, response, "auth_token");
        return ResponseEntity.ok(new ApiResponse(messageService.getMessage("logout.success")));
    }
}
