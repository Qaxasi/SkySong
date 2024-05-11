package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.ports.PasswordEncoderPort;

import java.util.Collections;

class UserFactory {

    private final PasswordEncoderPort passwordEncoder;

    UserFactory(PasswordEncoderPort passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    User createUser(RegisterRequest request, Role role) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Collections.singleton(role));
        return user;
    }
}
