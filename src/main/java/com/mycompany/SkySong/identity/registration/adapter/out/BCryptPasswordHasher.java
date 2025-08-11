package com.mycompany.SkySong.identity.registration.adapter.out;

import com.mycompany.SkySong.identity.registration.application.port.PasswordHasher;
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
