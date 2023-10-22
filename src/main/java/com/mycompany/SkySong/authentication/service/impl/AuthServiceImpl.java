package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.constants.ValidationPatterns;
import com.mycompany.SkySong.authentication.role.entity.UserRole;
import com.mycompany.SkySong.authentication.service.LoginService;
import com.mycompany.SkySong.authentication.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.role.entity.Role;
import com.mycompany.SkySong.authentication.role.repository.RoleDAO;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.dto.LoginRequest;
import com.mycompany.SkySong.authentication.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.service.RegistrationService;
import com.mycompany.SkySong.authentication.user.entity.User;
import com.mycompany.SkySong.authentication.user.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
public class RegisterServiceImpl implements RegistrationService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public RegistrationService(UserDAO userDAO, RoleDAO roleDAO,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    @Override
    public RegistrationResponse register(RegisterRequest registerRequest) {

        validateCredentials(registerRequest);

        checkForExistingCredentials(registerRequest);

        User user = createUserFromRequest(registerRequest);

        userDAO.save(user);

        return new RegistrationResponse("User registered successfully");

    }
    private void validateCredentials(RegisterRequest registerRequest) {
        validateUsername(registerRequest);
        validateEmail(registerRequest);
        validatePassword(registerRequest);
    }
    private void validateUsername(RegisterRequest registerRequest) {
        if (!registerRequest.username().matches(ValidationPatterns.USERNAME_PATTERN)) {
            throw new RegisterException("Invalid username format. The username can contain only letter and numbers.");
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
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        Role userRole = roleDAO.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new RegisterException("User role not set in the system!"));

        user.setRoles(Set.of(userRole));
        return user;
    }
}
