package com.mycompany.SkySong.role.repository;

import com.mycompany.SkySong.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDAO extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
