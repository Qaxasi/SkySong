package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserCreationImpl implements UserCreation {

    private final UserDAO userDAO;
    private final RoleManager roleManager;
    private final UserFactory userFactory;

    public UserCreationImpl(UserDAO userDAO, RoleManager roleManager, UserFactory userFactory) {
        this.userDAO = userDAO;
        this.roleManager = roleManager;
        this.userFactory = userFactory;
    }

    @Override
    public void createUser(RegisterRequest registerRequest) {

    }
}
