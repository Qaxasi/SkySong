package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationManagerImpl implements UserRegistrationManager {

    private final UserPersistence userPersistence;
    private final UserWithRoleSetup userSetup;

    public UserRegistrationManagerImpl(UserPersistence userPersistence, UserWithRoleSetup userSetup) {
        this.userPersistence = userPersistence;
        this.userSetup = userSetup;
    }

    @Transactional
    @Override
    public void setupNewUser(RegisterRequest registerRequest) {
        Role role = roleManager.getRoleByName(UserRole.ROLE_USER);
        User user = userFactory.createUser(registerRequest, role);

        int userId = userDAO.save(user);
        userDAO.assignRoleToUser(userId, role.getId());
    }
}
