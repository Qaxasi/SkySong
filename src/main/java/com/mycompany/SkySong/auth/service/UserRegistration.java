package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.RoleNotFoundException;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void registerUser(RegisterRequest request) {
        Role role = roleDAO.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));

        User user = userFactory.createUser(request, role);

        int userId = userDAO.save(user);

        for (Role roles : user.getRoles()) {
            userDAO.assignRoleToUser(userId, roles.getId());
        }
    }
}
