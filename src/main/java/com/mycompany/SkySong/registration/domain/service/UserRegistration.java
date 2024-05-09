package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.RoleNotFoundException;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import com.mycompany.SkySong.registration.domain.ports.RoleRepositoryPort;
import com.mycompany.SkySong.registration.domain.ports.UserRepositoryPort;
import org.springframework.transaction.support.TransactionTemplate;

class UserRegistration {

    private final UserFactory userFactory;
    private final RoleRepositoryPort roleRepository;
    private final UserRepositoryPort userRepository;
    private final TransactionTemplate transactionTemplate;

    public UserRegistration(UserFactory userFactory,
                            RoleRepositoryPort roleRepository,
                            UserRepositoryPort userRepository,
                            TransactionTemplate transactionTemplate) {
        this.userFactory = userFactory;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.transactionTemplate = transactionTemplate;
    }

    ApiResponse registerUser(RegisterRequest request) {
        return transactionTemplate.execute(status -> {
            Role role = roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                    () -> new RoleNotFoundException("Role not found"));

            User user = userFactory.createUser(request, role);

            int userId = userRepository.save(user);

            for (Role roles : user.getRoles()) {
                userRepository.assignRoleToUser(userId, roles.getId());
            }
            return new ApiResponse("User registered successfully.");
        });
    }
}