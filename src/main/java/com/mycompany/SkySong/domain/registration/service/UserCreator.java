package com.mycompany.SkySong.domain.registration.service;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.registration.ports.RegistrationRoleRepository;

import java.util.Collections;

public class UserCreator {

    private final PasswordEncoder passwordEncoder;
    private final RegistrationRoleRepository registrationRoleRepository;

    public UserCreator(PasswordEncoder passwordEncoder,
                       RegistrationRoleRepository registrationRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.registrationRoleRepository = registrationRoleRepository;
    }

    public User createUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Collections.singleton(fetchDefaultUserRole()));
        return user;
    }

    private Role fetchDefaultUserRole() {
        return registrationRoleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Default role not found: " + UserRole.ROLE_USER));
    }
}
