package com.mycompany.SkySong.identity.adapter.registration.out.security;

import com.mycompany.SkySong.identity.application.registration.ports.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder bcrypt;

    BCryptPasswordHasher(final BCryptPasswordEncoder encoder) {
        this.bcrypt = encoder;
    }

    @Override
    public String hash(final CharSequence password) {
        return bcrypt.encode(password);
    }
}
