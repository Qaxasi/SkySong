package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.common.entity.User;

import java.util.Optional;

public interface LoginUserRepository {
    Optional<User> findByUsername(String username);
}
