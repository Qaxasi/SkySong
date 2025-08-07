package com.mycompany.SkySong.identity.application.registration.ports;

import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.UserRole;

import java.util.Optional;

public interface RoleProvider {
    Optional<Role> provideRole(UserRole role);
}
