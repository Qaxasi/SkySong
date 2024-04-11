package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.auth.exception.RoleNotFoundException;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.auth.repository.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserBuilder {

    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PasswordEncoder encoder;

    private Integer id;
    private String username;
    private String email;

    public UserBuilder buildByUsername(String username) {
        return withUsername(username).withId(1).withEmail("user@mail.mail");
    }

    public UserBuilder buildByEmail(String email) {
        return withEmail(email).withId(1).withUsername("User");
    }

    public UserBuilder buildById(Integer id) {
        return withId(id).withUsername("User").withEmail("user@mail.mail");
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

        int id = userDAO.save(user);
        userDAO.assignRoleToUser(id, role.getId());
    }

    public void buildUser(String email) {
        Role role = roleDAO.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found"));
        Set<Role> roles = Set.of(role);

        User user = new User(1, "User", email, encoder.encode("Password#3"), roles);

        int id = userDAO.save(user);
        userDAO.assignRoleToUser(id, role.getId());
    }
}
