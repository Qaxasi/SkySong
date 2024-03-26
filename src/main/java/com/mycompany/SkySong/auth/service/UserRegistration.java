package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserRegistration {

    private final UserFactory userFactory;
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;

    public UserRegistration(UserFactory userFactory, RoleDAO roleDAO, UserDAO userDAO) {
        this.userFactory = userFactory;
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
    }
}
