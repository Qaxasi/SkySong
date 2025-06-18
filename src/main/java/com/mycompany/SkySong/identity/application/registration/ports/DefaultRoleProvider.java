package com.mycompany.SkySong.identity.application.registration.ports;

import com.mycompany.SkySong.identity.domain.Role;

import java.util.Optional;

public interface DefaultRoleProvider {
    Optional<Role> provideDefaultRole();
}
