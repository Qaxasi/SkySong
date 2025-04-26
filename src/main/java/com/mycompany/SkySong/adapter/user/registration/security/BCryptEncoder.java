package com.mycompany.SkySong.adapter.registration.security;


import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class BCryptEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt;

    BCryptEncoder(BCryptPasswordEncoder encoder) {
        this.bcrypt = encoder;
    }

    @Override
    public String encode(CharSequence password) {
        return bcrypt.encode(password);
    }
}
