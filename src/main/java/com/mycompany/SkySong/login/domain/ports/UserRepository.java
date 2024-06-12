package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.registration.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
