package com.mycompany.SkySong.authentication.repository;

import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDAO extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole roleName);
}
