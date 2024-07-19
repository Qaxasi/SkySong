package com.mycompany.SkySong.adapter.registration.adapters;


import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
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
