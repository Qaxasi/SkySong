package com.mycompany.SkySong.user.delete.domain.ports;

import com.mycompany.SkySong.common.dao.UserDAO;
import com.mycompany.SkySong.registration.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort extends UserDAO {
    Optional<User> findById(int userId);
    void deleteUserRoles(int userId);
    void delete(User user);
}
