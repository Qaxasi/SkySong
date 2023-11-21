package com.mycompany.SkySong.authentication.secutiry;

import com.mycompany.SkySong.authentication.exception.TokenException;
import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;
import io.jsonwebtoken.ExpiredJwtException;

public class JwtExceptionHandler {
    private final ApplicationMessageService messageService;

    public JwtExceptionHandler(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }
}
