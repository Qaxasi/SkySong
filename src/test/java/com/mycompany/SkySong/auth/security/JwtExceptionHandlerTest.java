package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtExceptionHandlerTest {
    @InjectMocks
    private JwtExceptionHandler handler;
    @Mock
    private ApplicationMessageService messageService;

    @Test
    void whenExpiredJwtException_ThrowTokenException() {
        ExpiredJwtException exception = new ExpiredJwtException(null, null, null);

        assertThrows(TokenException.class, () -> jwtExceptionHandler.handleException(exception));
    }
    @Test
    void whenMalformedJwtException_ThrowTokenException() {
        MalformedJwtException malformedJwtException = new MalformedJwtException("JWT is malformed");

        assertThrows(TokenException.class, () -> jwtExceptionHandler.handleException(malformedJwtException));
    }
    @Test
    void whenUnsupportedJwtException_ThrowTokenException() {
        UnsupportedJwtException unsupportedJwtException = new UnsupportedJwtException("JWT is unsupported");

        assertThrows(TokenException.class, () -> jwtExceptionHandler.handleException(unsupportedJwtException));
    }
    @Test
    void whenIllegalArgumentException_ThrowTokenException() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Claims are empty");

        assertThrows(TokenException.class, () -> jwtExceptionHandler.handleException(illegalArgumentException));
    }
    @Test
    void whenUnhandledExceptionType_NotIntercept() {
        NullPointerException exception = new NullPointerException("Error");

        assertDoesNotThrow(() -> jwtExceptionHandler.handleException(exception));
    }
}
