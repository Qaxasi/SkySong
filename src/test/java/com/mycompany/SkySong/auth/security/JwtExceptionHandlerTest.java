package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mycompany.SkySong.testsupport.auth.security.JwtExceptionHandlerTestHelper.assertThrowsTokenException;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtExceptionHandlerTest {
    @InjectMocks
    private JwtExceptionHandler handler;
    @Mock
    private ApplicationMessageService messageService;

    @Test
    void whenExpiredJwtException_ThrowTokenException() {
        assertThrowsTokenException(handler, new ExpiredJwtException(null, null, null));
    }
    @Test
    void whenMalformedJwtException_ThrowTokenException() {
        assertThrowsTokenException(handler, new MalformedJwtException("JWT is malformed");
    }
    @Test
    void whenUnsupportedJwtException_ThrowTokenException() {
        assertThrowsTokenException(handler, new UnsupportedJwtException("JWT is unsupported"));
    }
    @Test
    void whenIllegalArgumentException_ThrowTokenException() {
        assertThrowsTokenException(handler, new IllegalArgumentException("Claims are empty"));
    }
    @Test
    void whenUnhandledExceptionType_NotIntercept() {
        assertDoesNotThrow(() -> handler.handleException(new NullPointerException("Error")));
    }
}
