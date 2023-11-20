package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.exception.ServiceFailureException;
import com.mycompany.SkySong.authentication.service.CookieService;
import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;
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
    private final ApplicationMessageService messageService;
    public LogoutController(CookieService cookieService, ApplicationMessageService messageService) {
        this.cookieService = cookieService;
        this.messageService = messageService;
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            cookieService.deleteCookie(request, response, "auth_token");
            return ResponseEntity.ok(messageService.getMessage("logout.success"));
        } catch (RuntimeException e) {
            throw new ServiceFailureException(messageService.getMessage("logout.failure"));
        }
    }
}
