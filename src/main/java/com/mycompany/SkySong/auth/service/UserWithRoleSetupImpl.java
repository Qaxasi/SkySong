package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserWithRoleSetup {

    private final UserFactory userFactory;

    public UserWithRoleSetup(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public User setupUserWithRole(RegisterRequest request) {
        return userFactory.createUser(request, role);
    }
}
