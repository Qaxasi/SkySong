package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.RoleNotFoundException;
import com.mycompany.SkySong.common.entity.Role;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import com.mycompany.SkySong.registration.domain.ports.PasswordEncoder;
import com.mycompany.SkySong.registration.domain.ports.RegistrationRoleRepository;

import java.util.Collections;

class UserCreator {

    private final PasswordEncoder passwordEncoder;
    private final RegistrationRoleRepository registrationRoleRepository;

    UserCreator(PasswordEncoder passwordEncoder,
                RegistrationRoleRepository registrationRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.registrationRoleRepository = registrationRoleRepository;
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
        return registrationRoleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));
    }
}
