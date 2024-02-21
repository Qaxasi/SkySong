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
    private final RegistrationValidationService registrationValidator;
    private final ApplicationMessageService messageService;
    private final RoleManager roleManager;
    private final UserFactory userFactory;
    private final CredentialExistenceChecker credentialExistenceChecker;

    public RegistrationServiceImpl(UserDAO userDAO, RegistrationValidationService registrationValidator,
                                   ApplicationMessageService messageService,
                                   RoleManager roleManager, UserFactory userFactory,
                                   CredentialExistenceChecker credentialExistenceChecker) {
        this.userDAO = userDAO;
        this.registrationValidator = registrationValidator;
        this.messageService = messageService;
        this.roleManager = roleManager;
        this.userFactory = userFactory;
        this.credentialExistenceChecker = credentialExistenceChecker;
    }

    @Transactional
    @Override
    public ApiResponse register(RegisterRequest registerRequest) throws DatabaseException {

        registrationValidator.validateCredentials(registerRequest);

        credentialExistenceChecker.checkForExistingCredentials(registerRequest);

        Role role = roleManager.getRoleByName(UserRole.ROLE_USER);

        User user = userFactory.createUser(registerRequest, role);

        userDAO.save(user);

        return new ApiResponse(messageService.getMessage("user.registration.success"));
    }
}
