package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtExceptionHandler {
    private final ApplicationMessageService messageService;

    public JwtExceptionHandler(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }
    public void handleException(Exception e) {
        log.error("Error validating JWT token: {}", e.getMessage());

        throw new TokenException(messageService.getMessage("jwt.authentication.failed"));
    }
}
