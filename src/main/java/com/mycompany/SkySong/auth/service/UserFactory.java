package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
class UserFactory {

    private final PasswordEncoderService passwordEncoder;

    public UserFactory(PasswordEncoderService passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequest request, Role role) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encodePassword(request.password()));
        user.setRoles(Collections.singleton(role));
        return user;
    }
}
