package com.mycompany.SkySong.domain.registration.ports;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.enums.UserRole;

import java.util.Optional;

public interface RegistrationRoleRepository {
    Optional<Role> findByName(UserRole roleName);
}
