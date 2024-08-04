package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.function.Consumer;

public class UserFixture {

    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

    public UserFixture(RoleDAO roleDAO,
                       PasswordEncoder passwordEncoder,
                       UserDAO userDAO) {
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }

    public void createUserWithId(Integer id) {
        createUser(builder -> builder.withId(id));
    }

    public void createUserWithUsername(String username) {
        createUser(builder -> builder.withUsername(username));
    }

    public void createUserWithEmail(String email) {
        createUser(builder -> builder.withEmail(email));
    }
    public void createRegularUser() {
        createUser(builder -> builder.withRole(UserRole.ROLE_USER));
    }

    public void createAdminUser() {
        createUser(builder -> builder.withRole(UserRole.ROLE_ADMIN));
    }
    public void createUserWithUsernameAndPassword(String username, String password) {
        createUser(builder -> builder.withUsername(username).withPassword(password));
    }

    private void createUser(Integer id, String username, String email, String password, Role role) {
        User user = new User.Builder()
                .withId(id)
                .withUsername(username)
                .withEmail(email)
                .withPassword(password)
                .withRole(role)
                .build();

        saveUserAndAssignRoles(user);
    }
    private void saveUserAndAssignRoles(User user) {
        int userId = userDAO.save(user);
        user.getRoles().forEach(role -> userDAO.assignRoleToUser(userId, role.getId()));
    }

    private Role fetchRegularRole() {
        return roleDAO.findByName(UserRole.ROLE_USER).orElseThrow(
                () -> new RoleNotFoundException("Role not found: " + UserRole.ROLE_USER));
    }
    private Role fetchAdminRole() {
        return roleDAO.findByName(UserRole.ROLE_ADMIN).orElseThrow(
                () -> new RoleNotFoundException("Role not found: " + UserRole.ROLE_ADMIN));
    }
}
