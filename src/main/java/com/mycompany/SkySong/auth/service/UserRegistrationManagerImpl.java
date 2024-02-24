package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationManagerImpl implements UserRegistrationManager {

    private final UserDAO userDAO;
    private final RoleManager roleManager;
    private final UserFactory userFactory;

    public UserRegistrationManagerImpl(UserDAO userDAO, RoleManager roleManager, UserFactory userFactory) {
        this.userDAO = userDAO;
        this.roleManager = roleManager;
        this.userFactory = userFactory;
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
