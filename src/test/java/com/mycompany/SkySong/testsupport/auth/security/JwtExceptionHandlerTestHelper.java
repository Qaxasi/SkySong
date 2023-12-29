package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.JwtExceptionHandler;
import com.mycompany.SkySong.shared.exception.TokenException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtExceptionHandlerTestHelper {
    public static void assertThrowsTokenException(JwtExceptionHandler handler, Exception e) {
        assertThrows(TokenException.class, () -> handler.handleException(e));
    }
}
