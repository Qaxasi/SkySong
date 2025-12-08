package com.mycompany.skysong.identity.adapter.out.security.spring;

import com.mycompany.skysong.identity.application.registration.port.PasswordHasher;
import com.mycompany.skysong.identity.domain.RawPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordHasher implements PasswordHasher {
    private final BCryptPasswordEncoder bcrypt;

    BCryptPasswordHasher(final BCryptPasswordEncoder encoder) {
        this.bcrypt = encoder;
    }

    @Override
    public String hash(final RawPassword password) {
        return bcrypt.encode(password.asCharSequenceView());
    }
}
