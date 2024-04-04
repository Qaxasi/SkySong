package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.auth.exception.RoleNotFoundException;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import com.mycompany.SkySong.auth.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestUserFactory {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PasswordEncoder encoder;

    public void buildUser(int userId, String username) {
        Role role = roleDAO.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));
        Set<Role> roles = Set.of(role);

        User user = new User(userId, username, username + "@mail.mail", encoder.encode("Password#3"), roles);

        int id = userDAO.save(user);
        userDAO.assignRoleToUser(id, role.getId());
    }
}
