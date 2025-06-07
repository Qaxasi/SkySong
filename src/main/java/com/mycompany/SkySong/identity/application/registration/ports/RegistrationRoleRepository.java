package com.mycompany.SkySong.domain.user.registration.ports;

import com.mycompany.SkySong.identity.domain.Role;
import com.mycompany.SkySong.identity.domain.UserRole;

import java.util.Optional;

public interface RegistrationRoleRepository {
    Optional<Role> findByName(UserRole roleName);
}
