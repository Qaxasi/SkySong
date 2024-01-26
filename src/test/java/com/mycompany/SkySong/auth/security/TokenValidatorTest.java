package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import com.mycompany.SkySong.testsupport.auth.security.InvalidTokenGeneratorHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class TokenValidatorTest extends BaseIT {
    @Autowired
    private TokenValidator validator;
    @Autowired
    private InvalidTokenGeneratorHelper invalidTokenGenerator;
    @Autowired
    private TokenGeneratorHelper validTokenGenerator;
    @Test
    void whenValidToken_ValidationReturnTrue() {
        // given
        String token = validTokenGenerator.generateCorrectToken();

        // when
        boolean result = validator.validateToken(token);

        // then
        assertTrue(result);
    }
    @Test
    void whenExpiredToken_ValidationReturnFalse() {
        // given
        String token = invalidTokenGenerator.generateExpiredToken();

        // when
        boolean result = validator.validateToken(token);

        // then
        assertFalse(result);
    }
    @Test
    void whenMalformedToken_ValidationReturnFalse() {
        // given
        String token = invalidTokenGenerator.generateMalformedToken();

        // when
        boolean result = validator.validateToken(token);

        // then
        assertFalse(result);
    }
    @Test
    void whenTokenHasUnsupportedSignature_ValidationReturnFalse() {
        // given
        String token = invalidTokenGenerator.generateTokenWithUnsupportedSignature();

        // when
        boolean result = validator.validateToken(token);

        // then
        assertFalse(result);
    }

    @Test
    void whenTokenHasInvalidSignature_ValidationReturnFalse() {
        // given
        String token = invalidTokenGenerator.generateTokenWithInvalidSignature();

        // when
        boolean result = validator.validateToken(token);

        // then
        assertFalse(result);
    }

    @Test
    void whenTokenHasEmptyClaims_ValidationReturnFalse() {
        // given
        String token = invalidTokenGenerator.generateTokenWithEmptyClaims();

        // when
        boolean result = validator.validateToken(token);

        // then
        assertFalse(result);
    }
}
