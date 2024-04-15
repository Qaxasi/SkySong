package com.mycompany.SkySong.registration;

import com.mycompany.SkySong.user.Role;
import com.mycompany.SkySong.user.User;
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
