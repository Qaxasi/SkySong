package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import com.mycompany.SkySong.testsupport.auth.security.InvalidTokenGeneratorHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenValidatorTest extends BaseIT {
    @Autowired
    private TokenValidator validator;
    @Autowired
    private InvalidTokenGeneratorHelper invalidTokenGenerator;
    @Autowired
    private TokenGeneratorHelper validTokenGenerator;
    @Test
    void whenValidToken_ValidateIsCorrect() {
        // given
        String token = validTokenGenerator.generateCorrectToken();

        // when & then
        assertTrue(validator.validateToken(token));
    }
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
