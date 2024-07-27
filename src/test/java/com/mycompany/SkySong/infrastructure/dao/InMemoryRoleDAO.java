package com.mycompany.SkySong.infrastructure.dao;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InMemoryRoleDAO implements RoleDAO {

    private final Map<Long, Role> roles = new HashMap<>();

    @Override
    public Optional<Role> findByName(UserRole roleName) {
        return null;
    }

    @Override
    public Set<Role> findRolesByUserId(Integer userId) {
        return null;
    }
}
