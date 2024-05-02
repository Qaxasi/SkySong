package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.domain.exception.RoleNotFoundException;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import com.mycompany.SkySong.registration.domain.ports.RoleRepositoryPort;
import com.mycompany.SkySong.registration.domain.ports.UserRepositoryPort;

class UserRegistration {

    private final UserFactory userFactory;
    private final RoleRepositoryPort roleRepository;
    private final UserRepositoryPort userRepository;

    public UserRegistration(UserFactory userFactory,
                            RoleRepositoryPort roleRepository,
                            UserRepositoryPort userRepository) {
        this.userFactory = userFactory;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse registerUser(RegisterRequest request) {
        Role role = roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));

        User user = userFactory.createUser(request, role);

        int userId = userRepository.save(user);

        for (Role roles : user.getRoles()) {
            userRepository.assignRoleToUser(userId, roles.getId());
        }
        return new ApiResponse("User registered successfully.");
    }

    private void validate(RegisterRequest request) {
        if (!request.username().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new CredentialValidationException("Invalid username format. The username can contain only letters " +
                    "and numbers, and should be between 3 and 20 characters long.");
        }
        if (!request.email().matches("^[a-zA-Z0-9._%+-]{1,15}@[a-zA-Z0-9.-]{2,15}\\.[a-zA-Z]{2,6}$")) {
            throw new CredentialValidationException("Invalid email address format. The email should follow the standard " +
                    "format (e.g., user@example.com) and be between 6 and 30 characters long.");
        }
        if (!request.password().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            throw new CredentialValidationException("Invalid password format. The password must contain an least " +
                    "8 characters, including uppercase letters, lowercase letters, numbers, and special characters.");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new CredentialValidationException("Username is already exist!.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new CredentialValidationException("mail is already exist!.");
        }
    }
}