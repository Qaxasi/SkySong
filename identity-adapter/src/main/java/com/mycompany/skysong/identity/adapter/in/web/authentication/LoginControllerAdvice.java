package com.mycompany.skysong.identity.adapter.in.web.authentication;

import com.mycompany.skysong.web.response.ResponsePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = LoginController.class)
public class LoginControllerAdvice {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponsePayload<AuthResponse>> maskLoginInputErrors() {
        return LoginErrorResponses.unauthorizedErrorResponse();
    }
}
