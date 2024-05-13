package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.user.UserDAO;

import java.util.Optional;

public interface UserRepositoryPort extends UserDAO {
    Optional<User> findByUsername(String username);
}
