package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.mycompany.SkySong.infrastructure.web.HttpErrorMapper;
import com.mycompany.SkySong.infrastructure.web.contract.ResponsePayload;
import com.mycompany.SkySong.shared.error.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = LoginController.class)
public class LoginControllerAdvice {
    private final HttpErrorMapper mapper;

    public LoginControllerAdvice(final HttpErrorMapper mapper) {
        this.mapper = mapper;
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponsePayload<AuthResponse>> maskLoginInputErrors() {
        return mapper.failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
    }
}
