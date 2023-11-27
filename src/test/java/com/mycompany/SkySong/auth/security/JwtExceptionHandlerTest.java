package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtExceptionHandlerTest {
    @InjectMocks
    private JwtExceptionHandler jwtExceptionHandler;
    @Mock
    private ApplicationMessageService messageService;

    @Test
    void shouldThrowTokenExceptionForExpiredJwtException() {
        ExpiredJwtException exception = new ExpiredJwtException(null, null, null);

        assertThrows(TokenException.class, () -> jwtExceptionHandler.handleException(exception));
    }
    @Test
    void shouldReturnCorrectMessageForExpiredJwtException() {
        String expectedMessage = "token expired";

        when(messageService.getMessage("jwt.expired")).thenReturn(expectedMessage);

        ExpiredJwtException exception = new ExpiredJwtException(null, null, null);

        TokenException tokenException = assertThrows(TokenException.class,
                () -> jwtExceptionHandler.handleException(exception));

        assertEquals(expectedMessage, tokenException.getMessage());
    }

}
