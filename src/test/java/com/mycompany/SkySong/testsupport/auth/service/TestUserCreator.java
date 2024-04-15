package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.user.Role;
import com.mycompany.SkySong.user.User;
import com.mycompany.SkySong.user.UserRole;
import com.mycompany.SkySong.user.RoleDAO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestUserCreator {

    private final RoleDAO roleDAO;

    public TestUserCreator(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public User createUser(String username) {
        Role role = roleDAO.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new AssertionError("Role not found"));

        Set<Role> roles = Set.of(role);
        return new User( username, "new@mail.mail", "Password#3", roles);
    }
}
