package com.mycompany.SkySong.adapter.registration.adapters;

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
        String encodedPassword = encoder.encode(password);
        assertThat(password).isNotEqualTo(encodedPassword);
    }

    @Test
    void whenPasswordEncoded_MatchesAfterVerification() {
        String password = "Password#3";
        String encodedPassword = encoder.encode(password);
        assertThat(bCryptEncoder.matches(password, encodedPassword)).isTrue();
    }

    @Test
    void whenDifferentPasswordEncoded_thenDoesNotMatch() {
        String firstPassword = "FirstPassword#3";
        String secondPassword = "SecondPassword#3";

        String encodedPassword = encoder.encode(firstPassword);
        assertThat(bCryptEncoder.matches(secondPassword, encodedPassword)).isFalse();
    }

    private String encodePassword(CharSequence password) {
        return encoder.encode(password);
    }
}
