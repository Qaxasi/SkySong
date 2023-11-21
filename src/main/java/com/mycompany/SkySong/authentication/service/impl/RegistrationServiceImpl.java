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
    private final RoleDAO roleDAO;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationMessageService messageService;
    private final UserRoleManager userRoleManager;
    private final UserFactory userFactory;
    private final CredentialExistenceChecker credentialExistenceChecker;

    public RegistrationServiceImpl(UserDAO userDAO, RoleDAO roleDAO, ValidationService validationService,
                                   PasswordEncoder passwordEncoder, ApplicationMessageService messageService,
                                   UserRoleManager userRoleManager, UserFactory userFactory,
                                   CredentialExistenceChecker credentialExistenceChecker) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
        this.userRoleManager = userRoleManager;
        this.userFactory = userFactory;
        this.credentialExistenceChecker = credentialExistenceChecker;
    }

    @Transactional
    @Override
    public ApiResponse register(RegisterRequest registerRequest) {

        validationService.validateCredentials(registerRequest);

        checkForExistingCredentials(registerRequest);

        User user = createUserFromRequest(registerRequest);
        try {
            userDAO.save(user);
        } catch (DataAccessException ex) {
            throw new DatabaseException("An error occurred while processing your request. Please try again later.");
        }
        return new ApiResponse("User registered successfully");
    }
}
