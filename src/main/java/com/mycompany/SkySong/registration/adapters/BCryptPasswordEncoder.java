package com.mycompany.SkySong.registration.adapters;


import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class BCryptPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder encoder;

    BCryptPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(CharSequence password) {
        return encoder.encode(password);
    }
}
