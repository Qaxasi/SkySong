package com.mycompany.SkySong.infrastructure.dao;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDAO implements UserDAO {

    private final InMemoryRoleDAO roleDAO;

    private final Map<Integer, User> users = new HashMap<>();

    public InMemoryUserDAO(InMemoryRoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public int save(User user) {
        User newUser = new User.Builder()
                .withId(user.getId())
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withRoles(user.getRoles())
                .build();

        users.put(user.getId(), newUser);
        return user.getId();
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void deleteUserRoles(int userId) {
        roleDAO.deleteUserRoles(userId);
    }

    @Override
    public void assignRoleToUser(int userId, int roleId) {
        roleDAO.assignRoleToUser(userId, roleId);
    }

    public void clear() {
        users.clear();
    }
}
