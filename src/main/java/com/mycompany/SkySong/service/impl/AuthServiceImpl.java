package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.ex.RegisterException;
import com.mycompany.SkySong.role.entity.Role;
import com.mycompany.SkySong.role.repository.RoleDAO;
import com.mycompany.SkySong.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.service.AuthService;
import com.mycompany.SkySong.dto.LoginRequest;
import com.mycompany.SkySong.dto.RegisterRequest;
import com.mycompany.SkySong.user.entity.User;
import com.mycompany.SkySong.user.repository.UserDAO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.usernameOrEmail(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterRequest registerRequest) {

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

        Set<Role> roles = new HashSet<>();
        Role userRole = roleDAO.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userDAO.save(user);

        return "User registered successfully";

    }
}
