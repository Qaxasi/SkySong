package com.mycompany.SkySong.domain.registration.service;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.domain.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.registration.ports.RegistrationRoleRepository;

public class UserCreator {

    private final PasswordEncoder passwordEncoder;
    private final RegistrationRoleRepository roleRepository;

    public UserCreator(PasswordEncoder passwordEncoder,
                       RegistrationRoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(RegisterRequest request) {
        return new User.Builder()
                .withUsername(request.username())
                .withEmail(request.email())
                .withPassword(passwordEncoder.encode(request.password()))
                .withRole(fetchDefaultUserRole())
                .build();
    }

    private Role fetchDefaultUserRole() {
        return roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Default role not found: " + UserRole.ROLE_USER));
    }
}
