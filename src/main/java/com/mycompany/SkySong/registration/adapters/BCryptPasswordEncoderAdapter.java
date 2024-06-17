package com.mycompany.SkySong.registration.adapters;


import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class BCryptPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    BCryptPasswordEncoder() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence password) {
        return encoder.encode(password);
    }
}
