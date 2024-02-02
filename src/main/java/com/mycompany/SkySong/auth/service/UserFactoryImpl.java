package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.shared.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
class UserFactoryImpl implements UserFactory {

    private final PasswordService passwordService;

    public UserFactoryImpl(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public User createUser(RegisterRequest registerRequest, Role role) {
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordService.encodePassword(registerRequest.password()));
        user.setRoles(Collections.singleton(role));
        return user;
    }
}
