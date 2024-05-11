package com.mycompany.SkySong.registration.adapters;


import com.mycompany.SkySong.registration.domain.ports.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder encoder;

    PasswordEncoderAdapter(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(CharSequence password) {
        return encoder.encode(password);
    }
}
