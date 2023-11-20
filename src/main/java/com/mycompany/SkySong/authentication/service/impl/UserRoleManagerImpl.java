package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.repository.RoleDAO;
import com.mycompany.SkySong.authentication.service.UserRoleManager;
import org.springframework.stereotype.Service;

@Service
public class UserRoleManagerImpl implements UserRoleManager {
    private final RoleDAO roleDAO;
    private final ApplicationMessageService messageService;

    public UserRoleManagerImpl(RoleDAO roleDAO, ApplicationMessageService messageService) {
        this.roleDAO = roleDAO;
        this.messageService = messageService;
    }

    @Override
    public Role getUserRole() {
        return null;
    }
}
