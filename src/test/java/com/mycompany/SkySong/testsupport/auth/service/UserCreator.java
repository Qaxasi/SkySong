package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.auth.repository.RoleDAO;
import org.springframework.stereotype.Component;

@Component
public class UserCreator {

    private final RoleDAO roleDAO;

    public UserCreator(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
}
