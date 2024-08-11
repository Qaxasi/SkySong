package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import com.mycompany.SkySong.domain.shared.entity.Role;
import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;

public class UserFixture {
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;
    private final UserBuilder userBuilder;

    public UserFixture(RoleDAO roleDAO,
                       UserDAO userDAO,
                       UserBuilder userBuilder) {
        this.roleDAO = roleDAO;
        this.userDAO = userDAO;
        this.userBuilder = userBuilder;
    }

    public void createUserWithId(Integer id) {
        createUser(userBuilder.copy().withId(id), fetchRegularRole());
    }

    public void createUserWithUsername(String username) {
        createUser(userBuilder.copy().withUsername(username), fetchRegularRole());
    }

    public void createUserWithEmail(String email) {
        createUser(userBuilder.copy().withEmail(email), fetchRegularRole());
    }

    public void createRegularUser() {
        createUser(userBuilder.copy(), fetchRegularRole());
    }

    public void createAdminUser() {
        createUser(userBuilder.copy(), fetchAdminRole());
    }

    public void createUserWithUsernameAndPassword(String username, String password) {
        createUser(userBuilder.copy().withUsername(username).withPassword(password), fetchRegularRole());
    }

    private User createUser(UserBuilder builder, Role role) {
        User user = builder.withRole(role).build();
        saveUserAndAssignRoles(user);
        return user;
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
