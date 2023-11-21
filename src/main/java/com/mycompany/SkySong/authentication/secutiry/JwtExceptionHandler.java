package com.mycompany.SkySong.authentication.secutiry;

import com.mycompany.SkySong.authentication.service.impl.ApplicationMessageService;

public class JwtExceptionHandler {
    private final ApplicationMessageService applicationMessageService;

    public JwtExceptionHandler(ApplicationMessageService applicationMessageService) {
        this.applicationMessageService = applicationMessageService;
    }
}
