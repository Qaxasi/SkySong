package com.mycompany.SkySong.security.exception;


import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}