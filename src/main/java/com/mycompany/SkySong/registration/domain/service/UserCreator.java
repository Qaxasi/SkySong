package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.RoleNotFoundException;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import com.mycompany.SkySong.registration.domain.ports.RoleRepository;

import java.util.Collections;

class UserCreator {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    UserCreator(PasswordEncoder passwordEncoder,
                RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    User createUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Collections.singleton(fetchDefaultUserRole()));
        return user;
    }

    private Role fetchDefaultUserRole() {
        return roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));
    }
}
