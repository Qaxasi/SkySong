package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserFactory {

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
