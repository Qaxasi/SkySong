package com.mycompany.SkySong.adapter.registration.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordEncoderTest {
    private BCryptEncoder encoder;
    private BCryptPasswordEncoder bCryptEncoder;

    @BeforeEach
    void setup() {
        encoder = new BCryptEncoder(bCryptEncoder);
    }
}
