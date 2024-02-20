package com.mycompany.SkySong.auth.repository;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDAO {
    Optional<Role> findByName(UserRole roleName);
}
