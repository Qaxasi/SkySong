package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.exception.DatabaseException;
import com.mycompany.SkySong.authentication.exception.ServiceFailureException;
import com.mycompany.SkySong.authentication.utils.constants.ValidationPatterns;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.model.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.repository.RoleDAO;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.service.RegistrationService;
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
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(UserDAO userDAO, RoleDAO roleDAO,
                                   PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional
    @Override
    public RegistrationResponse register(RegisterRequest registerRequest) {

        validateCredentials(registerRequest);

        checkForExistingCredentials(registerRequest);

        User user = createUserFromRequest(registerRequest);
        try {
            userDAO.save(user);
        } catch (DataAccessException ex) {
            throw new DatabaseException("An error occurred while processing your request. Please try again later.");
        }
        return new RegistrationResponse("User registered successfully");
    }

    private void validateCredentials(RegisterRequest registerRequest) {
        validateUsername(registerRequest);
        validateEmail(registerRequest);
        validatePassword(registerRequest);
    }

    private void validateUsername(RegisterRequest registerRequest) {
        if (!registerRequest.username().matches(ValidationPatterns.USERNAME_PATTERN)) {
            throw new RegisterException("Invalid username format. The username can contain only letters and numbers, " +
                    "and should be between 3 to 20 characters long.");
        }
    }

    private void validateEmail(RegisterRequest registerRequest) {
        if (!registerRequest.email().matches(ValidationPatterns.EMAIL_PATTERN)) {
            throw new RegisterException("Invalid email address format. The email should follow " +
                    "the standard format (e.g., user@example.com).");
        }
    }

    private void validatePassword(RegisterRequest registerRequest) {
        if (!registerRequest.password().matches(ValidationPatterns.PASSWORD_PATTERN)) {
            throw new RegisterException("Invalid password format. The password must contain an least 8 characters," +
                    " including uppercase letters, lowercase letters, numbers, and special characters.");
        }
    }

    private void checkForExistingCredentials(RegisterRequest registerRequest) {
        checkForExistingUsername(registerRequest);
        checkForExistingEmail(registerRequest);
    }

    private void checkForExistingUsername(RegisterRequest registerRequest) {
        if (userDAO.existsByUsername(registerRequest.username())) {
            throw new RegisterException("Username is already exist!.");
        }
    }

    private void checkForExistingEmail(RegisterRequest registerRequest) {
        if (userDAO.existsByEmail(registerRequest.email())) {
            throw new RegisterException("Email is already exist!.");
        }
    }
    private User createUserFromRequest(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(encodePassword(registerRequest.password()));
        user.setRoles(Set.of(getUserRole()));

        return user;
    }
    private String encodePassword(String password) {
        try {
            return passwordEncoder.encode(password);
        } catch (Exception ex) {
            log.error("Error encoding the password");
            throw new ServiceFailureException("There was an issue during password encoding. Please try again later.");

        }
    }
    private Role getUserRole() {
        return roleDAO.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> {
                    log.error("User role not set in the system!");
                    return new ServiceFailureException(
                            "There was an issue during registration. Please try again later.");
                });
    }
}
