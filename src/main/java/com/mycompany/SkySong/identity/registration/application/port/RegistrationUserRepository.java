package com.mycompany.SkySong.identity.registration.application.port;

import com.mycompany.SkySong.identity.registration.application.dto.UniquenessStatus;

public interface RegistrationUserRepository {
    UniquenessStatus checkUniqueness(String username, String email);
}
