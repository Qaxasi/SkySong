package com.mycompany.SkySong.adapter.exception.security;

import 	org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
