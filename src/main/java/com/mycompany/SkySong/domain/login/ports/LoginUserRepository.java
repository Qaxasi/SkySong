package com.mycompany.SkySong.domain.login.ports;

import com.mycompany.SkySong.domain.shared.entity.User;

import java.util.Optional;

public interface LoginUserRepository {
    Optional<User> findByUsername(String username);
}
