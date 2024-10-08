package com.mycompany.SkySong.adapter.registration.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {
    private BCryptEncoder encoder;
    private BCryptPasswordEncoder bCryptEncoder;

    @BeforeEach
    void setup() {
        bCryptEncoder = new BCryptPasswordEncoder();
        encoder = new BCryptEncoder(bCryptEncoder);
    }

    @Test
    void whenPasswordEncoded_EncodedPasswordIsDifferent() {
        String password = "Password#3";
        String encodedPassword = encodePassword(password);
        assertThat(password).isNotEqualTo(encodedPassword);
    }

    @Test
    void whenPasswordEncoded_MatchesAfterVerification() {
        String password = "Password#3";
        String encodedPassword = encodePassword(password);
        assertThat(passwordMatches(password, encodedPassword)).isTrue();
    }

    @Test
    void whenDifferentPasswordEncoded_thenDoesNotMatch() {
        String firstPassword = "FirstPassword#3";
        String secondPassword = "SecondPassword#3";

        String encodedPassword = encodePassword(firstPassword);
        assertThat(passwordMatches(secondPassword, encodedPassword)).isFalse();
    }

    private String encodePassword(CharSequence password) {
        return encoder.encode(password);
    }

    private boolean passwordMatches(CharSequence rawPassword, String encodedPassword) {
        return bCryptEncoder.matches(rawPassword, encodedPassword);
    }
}
