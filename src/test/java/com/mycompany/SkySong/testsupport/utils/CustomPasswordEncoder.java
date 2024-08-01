package com.mycompany.SkySong.testsupport.utils;

import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public CustomPasswordEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(CharSequence password) {
       return encoder.encode(password);
    }
}
