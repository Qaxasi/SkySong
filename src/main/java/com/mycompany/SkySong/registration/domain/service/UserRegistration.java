package com.mycompany.SkySong.registration.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.exception.CredentialValidationException;
import com.mycompany.SkySong.registration.domain.exception.RoleNotFoundException;
import com.mycompany.SkySong.registration.domain.model.Role;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.registration.domain.model.UserRole;
import com.mycompany.SkySong.user.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistration {

    private final UserFactory userFactory;
    private final RoleRepository roleRepository;
    private final UserDAO userDAO;

    public UserRegistration(UserFactory userFactory,
                            RoleRepository roleRepository, UserDAO userDAO) {
        this.userFactory = userFactory;
        this.roleRepository = roleRepository;
        this.userDAO = userDAO;
    }

    public ApiResponse registerUser(RegisterRequest request) {
        Role role = roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));

        User user = userFactory.createUser(request, role);

        int userId = userDAO.save(user);

        for (Role roles : user.getRoles()) {
            userDAO.assignRoleToUser(userId, roles.getId());
        }
        return new ApiResponse("User registered successfully.");
    }
}