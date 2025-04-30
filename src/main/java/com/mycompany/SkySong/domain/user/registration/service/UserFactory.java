package com.mycompany.SkySong.domain.user.registration.service;

import com.mycompany.SkySong.domain.user.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.user.registration.model.UserRegistrationData;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.domain.user.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.user.registration.ports.RegistrationRoleRepository;

public class UserCreator {

    private final PasswordEncoder passwordEncoder;
    private final RegistrationRoleRepository roleRepository;

    public UserCreator(PasswordEncoder passwordEncoder,
                       RegistrationRoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(UserRegistrationData data) {
        return new User.Builder()
                .withUsername(data.username())
                .withEmail(data.email())
                .withPassword(passwordEncoder.encode(data.password()))
                .withRole(fetchDefaultUserRole())
                .build();
    }

    private Role fetchDefaultUserRole() {
        return roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("An error occurred during registration. Please try again later."));
    }
}
