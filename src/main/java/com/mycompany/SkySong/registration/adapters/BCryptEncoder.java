package com.mycompany.SkySong.registration.adapters;


import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class BCryptEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    BCryptEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }


    @Override
    public String encode(CharSequence password) {
        return encoder.encode(password);
    }
}
