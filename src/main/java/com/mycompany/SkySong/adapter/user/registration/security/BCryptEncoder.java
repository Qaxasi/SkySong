package com.mycompany.SkySong.adapter.user.registration.security;


import com.mycompany.SkySong.domain.user.registration.ports.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class BCryptEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt;

    BCryptEncoder(final BCryptPasswordEncoder encoder) {
        this.bcrypt = encoder;
    }

    @Override
    public String encode(final CharSequence password) {
        return bcrypt.encode(password);
    }
}
