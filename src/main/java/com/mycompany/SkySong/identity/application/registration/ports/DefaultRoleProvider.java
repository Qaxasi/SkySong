package com.mycompany.SkySong.identity.application.registration.ports;

import com.mycompany.SkySong.identity.domain.Role;

public interface DefaultRoleProvider {
    Role provideDefaultRole();
}
