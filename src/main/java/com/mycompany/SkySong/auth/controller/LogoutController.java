package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.auth.security.CookieService;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
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
            log.error("Error while logging out: " + e.getMessage(), e);
            throw new InternalErrorException(messageService.getMessage("logout.failure"));
        }
    }
}
