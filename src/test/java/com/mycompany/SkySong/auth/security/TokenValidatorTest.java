package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.security.TokenValidatorTestHelper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TokenValidatorTest extends BaseIT {
    @Autowired
    private TokenValidator validator;
    @Test
    void whenExpiredToken_ThrowException() {
        // given
        String token = TokenValidatorTestHelper.generateExpiredToken();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
    @Test
    void whenMalformedToken_ThrowException() {
        // given
        String token = TokenValidatorTestHelper.generateMalformedToken();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
    @Test
    void whenTokenHasUnsupportedSignature_ThrowException() {
        // given
        String token = TokenValidatorTestHelper.generateTokenWithUnsupportedSignature();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }

    @Test
    void whenTokenHasInvalidSignature_ThrowException() {
        // given
        String token = TokenValidatorTestHelper.generateTokenWithInvalidSignature();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }

    @Test
    void whenTokenHasEmptyClaims_ThrowException() {
        // given
        String token = TokenValidatorTestHelper.generateTokenWithEmptyClaims();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
}
