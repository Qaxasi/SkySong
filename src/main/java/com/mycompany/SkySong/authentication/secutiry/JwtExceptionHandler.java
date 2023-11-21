package com.mycompany.SkySong.authentication.secutiry;

import com.mycompany.SkySong.authentication.exception.TokenException;
import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

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
