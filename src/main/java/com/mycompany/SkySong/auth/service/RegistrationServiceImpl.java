package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.DatabaseException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
class RegistrationServiceImpl implements RegistrationService {
    private final UserDAO userDAO;
    private final CredentialsValidationService validation;
    private final ApplicationMessageService messageService;
    private final RoleManager roleManager;
    private final UserFactory userFactory;
    private final CredentialsExistenceChecker checker;

    public RegistrationServiceImpl(UserDAO userDAO, CredentialsValidationService validation,
                                   ApplicationMessageService messageService,
                                   RoleManager roleManager, UserFactory userFactory,
                                   CredentialsExistenceChecker checker) {
        this.userDAO = userDAO;
        this.validation = validation;
        this.messageService = messageService;
        this.roleManager = roleManager;
        this.userFactory = userFactory;
        this.checker = checker;
    }

    @Transactional
    @Override
    public ApiResponse register(RegisterRequest registerRequest) throws DatabaseException {
        validation.validateCredentials(registerRequest);
        checker.checkForExistingCredentials(registerRequest);

        Role role = roleManager.getRoleByName(UserRole.ROLE_USER);
        User user = userFactory.createUser(registerRequest, role);

        int userId = userDAO.save(user);
        userDAO.assignRoleToUser(userId, role.getId());

        return new ApiResponse(messageService.getMessage("user.registration.success"));
    }
}
