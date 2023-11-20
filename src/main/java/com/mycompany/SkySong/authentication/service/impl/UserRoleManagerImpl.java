package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.UserRoleManager;
import org.springframework.stereotype.Service;

@Service
public class UserRoleManagerImpl implements UserRoleManager {
    private final UserDAO userDAO;
    private final ApplicationMessageService messageService;

    public UserRoleManagerImpl(UserDAO userDAO, ApplicationMessageService messageService) {
        this.userDAO = userDAO;
        this.messageService = messageService;
    }

    @Override
    public Role getUserRole() {
        return null;
    }
}
