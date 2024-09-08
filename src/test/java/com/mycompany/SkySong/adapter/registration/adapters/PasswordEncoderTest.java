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
}
