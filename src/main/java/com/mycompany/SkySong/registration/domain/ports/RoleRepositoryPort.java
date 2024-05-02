package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import com.mycompany.SkySong.user.RoleDAO;

import java.util.Optional;
import java.util.Set;

public interface RoleRepositoryPort extends RoleDAO {
    Optional<Role> findByName(UserRole roleName);
    Set<Role> findRolesByUserId(Integer userId);
}
