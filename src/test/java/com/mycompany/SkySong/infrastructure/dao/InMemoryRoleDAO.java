package com.mycompany.SkySong.infrastructure.dao;

import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRoleDAO implements RoleDAO {

    private final Map<Integer, Role> roles = new HashMap<>();
    private final Map<Integer, Set<Integer>> userRoles = new HashMap<>();

    @Override
    public Optional<Role> findByName(UserRole roleName) {
        return roles.values().stream()
                .filter(role -> role.getName().equals(roleName))
                .findFirst();
    }

    @Override
    public Set<Role> findRolesByUserId(Integer userId) {
        return userRoles.getOrDefault(userId, Collections.emptySet()).stream()
                .map(roles::get)
                .collect(Collectors.toSet());
    }

    public void save(Role role) {
        roles.put(role.getId(), role);
    }

    public void assignRoleToUser(Integer userId, Integer roleId) {
        userRoles.computeIfAbsent(userId, k -> new HashSet<>()).add(roleId);
    }

    public void deleteUserRoles(Integer userId) {
        userRoles.remove(userId);
    }

    public void clear() {
        roles.clear();
        userRoles.clear();
    }
}
