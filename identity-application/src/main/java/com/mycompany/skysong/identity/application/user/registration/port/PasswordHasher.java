package com.mycompany.skysong.identity.application.user.registration.port;

import com.mycompany.skysong.identity.domain.RawPassword;

public interface PasswordHasher {
    String hash(RawPassword password);
}
