package com.mycompany.SkySong.registration;

import com.mycompany.SkySong.user.Role;
import com.mycompany.SkySong.user.User;
import com.mycompany.SkySong.user.UserRole;
import com.mycompany.SkySong.user.RoleDAO;
import com.mycompany.SkySong.user.UserDAO;
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
