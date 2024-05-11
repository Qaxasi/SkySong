package com.mycompany.SkySong.registration.adapters;


import com.mycompany.SkySong.registration.domain.ports.PasswordEncoderPort;
import org.springframework.stereotype.Service;

@Service
class PasswordEncoderAdapter implements PasswordEncoderPort {
    @Override
    public String encode(CharSequence password) {
        return null;
    }
}
