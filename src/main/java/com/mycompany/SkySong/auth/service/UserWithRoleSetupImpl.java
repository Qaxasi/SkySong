package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserWithRoleSetup {

    private final RoleManager roleManager;
    private final UserFactory userFactory;

    public UserWithRoleSetup(RoleManager roleManager, UserFactory userFactory) {
        this.roleManager = roleManager;
        this.userFactory = userFactory;
    }

    public User setupUserWithRole(RegisterRequest request) {
        Role role = roleManager.getRoleByName(UserRole.ROLE_USER);
        return userFactory.createUser(request, role);
    }
}
