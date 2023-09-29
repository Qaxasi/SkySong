package com.mycompany.SkySong.service.impl;

import com.mycompany.SkySong.role.repository.RoleDAO;
import com.mycompany.SkySong.service.AuthService;
import com.mycompany.SkySong.user.entity.LoginRequest;
import com.mycompany.SkySong.user.entity.RegisterRequest;
import com.mycompany.SkySong.user.repository.UserDAO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.usernameOrEmail(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "Successfully logged in.";
    }
}
