package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserWithRoleSetupImpl implements UserWithRoleSetup {

    private final RoleManager roleManager;
    private final UserFactory userFactory;

    public UserWithRoleSetupImpl(RoleManager roleManager, UserFactory userFactory) {
        this.roleManager = roleManager;
        this.userFactory = userFactory;
    }

    @Override
    public User setupUserWithRole(RegisterRequest request) {
        Role role = roleManager.getRoleByName(UserRole.ROLE_USER);
        return userFactory.createUser(request, role);
    }
}
