package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.DatabaseException;
import com.mycompany.SkySong.authentication.exception.ServiceFailureException;
import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.repository.RoleDAO;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.service.*;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserDAO userDAO;
    private final ValidationService validationService;
    private final ApplicationMessageService messageService;
    private final UserRoleManager userRoleManager;
    private final UserFactory userFactory;
    private final CredentialExistenceChecker credentialExistenceChecker;

    public RegistrationServiceImpl(UserDAO userDAO, ValidationService validationService,
                                   ApplicationMessageService messageService,
                                   UserRoleManager userRoleManager, UserFactory userFactory,
                                   CredentialExistenceChecker credentialExistenceChecker) {
        this.userDAO = userDAO;
        this.validationService = validationService;
        this.messageService = messageService;
        this.userRoleManager = userRoleManager;
        this.userFactory = userFactory;
        this.credentialExistenceChecker = credentialExistenceChecker;
    }

    @Transactional
    @Override
    public ApiResponse register(RegisterRequest registerRequest) {

        validationService.validateCredentials(registerRequest);

        credentialExistenceChecker.checkForExistingCredentials(registerRequest);

        Role role = userRoleManager.getRoleByName(UserRole.ROLE_USER);

        User user = userFactory.createUser(registerRequest, role);

        try {
            userDAO.save(user);
        } catch (DataAccessException ex) {
            throw new DatabaseException(messageService.getMessage("user.registration.error"));
        }
        return new ApiResponse(messageService.getMessage("user.registration.success"));
    }
}
