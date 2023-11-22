package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;

@Component
public class JwtExceptionHandler {
    private final ApplicationMessageService messageService;

    public JwtExceptionHandler(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }
    public void handleException(Exception e) {
        if (e instanceof ExpiredJwtException) {
            throw new TokenException(messageService.getMessage("jwt.expired"));
        } else if (e instanceof MalformedJwtException) {
            throw new TokenException(messageService.getMessage("jwt.invalid"));
        } else if (e instanceof UnsupportedJwtException) {
            throw new TokenException(messageService.getMessage("jwt.unsupported"));
        } else if (e instanceof IllegalArgumentException) {
            throw new TokenException(messageService.getMessage("jwt.claims.empty"));
        }
    }
}
