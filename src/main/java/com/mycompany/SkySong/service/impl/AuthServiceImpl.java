package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.dto.RegistrationResponse;
import com.mycompany.SkySong.ex.RegisterException;
import com.mycompany.SkySong.role.entity.Role;
import com.mycompany.SkySong.role.repository.RoleDAO;
import com.mycompany.SkySong.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.service.AuthService;
import com.mycompany.SkySong.dto.LoginRequest;
import com.mycompany.SkySong.dto.RegisterRequest;
import com.mycompany.SkySong.user.entity.User;
import com.mycompany.SkySong.user.repository.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserDAO userDAO, RoleDAO roleDAO,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.usernameOrEmail(), loginRequest.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            return token;
        } catch (Exception e) {
            log.error("Error during login for user: {}", loginRequest.usernameOrEmail(), e);
            throw e;
        }
    }

    @Override
    public RegistrationResponse register(RegisterRequest registerRequest) {

        if (userDAO.existsByUsername(registerRequest.username())) {
            throw new RegisterException("Username is already exists!.");
        }

        if (userDAO.existsByEmail(registerRequest.email())) {
            throw new RegisterException("Email is already exists!.");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));


        Role userRole = roleDAO.findByName("ROLE_USER")
                .orElseThrow(() -> new RegisterException("User role not set in the system!"));

        user.setRoles(Set.of(userRole));

        userDAO.save(user);

        return new RegistrationResponse(true, "User registered successfully");

    }
}
