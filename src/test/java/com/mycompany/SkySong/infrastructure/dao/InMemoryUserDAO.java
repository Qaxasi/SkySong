package com.mycompany.SkySong.infrastructure.dao;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDAO implements UserDAO {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public int save(User user) {
        id++;
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteUserRoles(int userId) {

    }

    @Override
    public void assignRoleToUser(int userId, int roleId) {

    }
}
