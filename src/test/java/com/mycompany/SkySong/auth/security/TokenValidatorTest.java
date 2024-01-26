package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import com.mycompany.SkySong.testsupport.auth.security.InvalidTokenGeneratorHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenValidatorTest {
    @Autowired
    private TokenValidator validator;
    @Autowired
    private InvalidTokenGeneratorHelper invalidTokenGenerator;
    @Autowired
    private TokenGeneratorHelper validTokenGenerator;
    @Test
    void whenExpiredToken_ThrowException() {
        // given
        String token = invalidTokenGenerator.generateExpiredToken();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
    @Test
    void whenMalformedToken_ThrowException() {
        // given
        String token = invalidTokenGenerator.generateMalformedToken();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
    @Test
    void whenTokenHasUnsupportedSignature_ThrowException() {
        // given
        String token = invalidTokenGenerator.generateTokenWithUnsupportedSignature();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }

    @Test
    void whenTokenHasInvalidSignature_ThrowException() {
        // given
        String token = invalidTokenGenerator.generateTokenWithInvalidSignature();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }

    @Test
    void whenTokenHasEmptyClaims_ThrowException() {
        // given
        String token = invalidTokenGenerator.generateTokenWithEmptyClaims();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
}
