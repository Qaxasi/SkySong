package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.exception.TokenException;
import com.mycompany.SkySong.testsupport.auth.security.TokenGeneratorHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
public class TokenValidatorTest {
    @Autowired
    private TokenValidator validator;
    @Test
    void whenExpiredToken_ThrowException() {
        // given
        String token = TokenGeneratorHelper.generateExpiredToken();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
    @Test
    void whenMalformedToken_ThrowException() {
        // given
        String token = TokenGeneratorHelper.generateMalformedToken();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
    @Test
    void whenTokenHasUnsupportedSignature_ThrowException() {
        // given
        String token = TokenGeneratorHelper.generateTokenWithUnsupportedSignature();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }

    @Test
    void whenTokenHasInvalidSignature_ThrowException() {
        // given
        String token = TokenGeneratorHelper.generateTokenWithInvalidSignature();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }

    @Test
    void whenTokenHasEmptyClaims_ThrowException() {
        // given
        String token = TokenGeneratorHelper.generateTokenWithEmptyClaims();

        // when & then
        assertThrows(TokenException.class, () -> validator.validateToken(token));
    }
}
