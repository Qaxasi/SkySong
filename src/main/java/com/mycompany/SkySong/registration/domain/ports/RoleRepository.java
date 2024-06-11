package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.UserRole;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(UserRole roleName);
}
