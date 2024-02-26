package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
class UserFactoryImpl implements UserFactory {

    private final PasswordService passwordService;

    public UserFactoryImpl(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public User createUser(RegisterRequest request, Role role) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordService.encodePassword(request.password()));
        user.setRoles(Collections.singleton(role));
        return user;
    }
}
