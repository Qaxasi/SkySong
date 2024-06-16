package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.common.entity.Role;
import com.mycompany.SkySong.common.enums.UserRole;

import java.util.Optional;

public interface RegistrationRoleRepository {
    Optional<Role> findByName(UserRole roleName);
}
