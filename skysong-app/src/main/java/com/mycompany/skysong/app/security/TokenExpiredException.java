package com.mycompany.skysong.app.security.exception;


import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}

// what to do with this all package