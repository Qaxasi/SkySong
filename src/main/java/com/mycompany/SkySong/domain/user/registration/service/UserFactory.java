package com.mycompany.SkySong.domain.user.registration.service;

import com.mycompany.SkySong.domain.user.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.user.registration.model.UserRegistrationData;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.domain.user.registration.ports.PasswordEncoder;
import com.mycompany.SkySong.domain.user.registration.ports.RegistrationRoleRepository;
import com.mycompany.SkySong.shared.error.ErrorType;

public class UserFactory {
    private final PasswordEncoder passwordEncoder;
    private final RegistrationRoleRepository roleRepository;

    public UserFactory(final PasswordEncoder passwordEncoder,
                       final RegistrationRoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(final UserRegistrationData data) {
        return new User.Builder()
                .withUsername(data.username())
                .withEmail(data.email())
                .withPassword(passwordEncoder.encode(data.password()))
                .withRole(fetchDefaultUserRole())
                .build();
    }

    private Role fetchDefaultUserRole() {
        return roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Default user role not found in the system.",
                                ErrorType.DEFAULT_ROLE_NOT_FOUND));
    }
}
