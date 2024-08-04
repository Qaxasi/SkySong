package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import java.util.function.Consumer;

public class UserFixture {

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

    private void createUser(Consumer<UserBuilder> config) {
        UserBuilder builderCopy = userBuilder.copy();
        config.accept(builderCopy);
        User user = builderCopy.build();
        saveUserAndAssignRoles(user);
    }
    private void saveUserAndAssignRoles(User user) {
        transactionTemplate.executeWithoutResult(status -> {
            int userId = userDAO.save(user);
            user.getRoles().forEach(role -> userDAO.assignRoleToUser(userId, role.getId()));
        });
    }
}
