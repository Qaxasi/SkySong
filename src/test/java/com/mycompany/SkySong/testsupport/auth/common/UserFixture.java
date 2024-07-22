package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.domain.shared.entity.User;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.SessionDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;

@Component
public class UserFixture {

    private final UserBuilder userBuilder;
    private final SessionBuilder sessionBuilder;
    private final TransactionTemplate transactionTemplate;

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;

    public UserFixture(UserBuilder userBuilder,
                       SessionBuilder sessionBuilder,
                       TransactionTemplate transactionTemplate,
                       UserDAO userDAO,
                       SessionDAO sessionDAO) {
        this.userBuilder = userBuilder;
        this.sessionBuilder = sessionBuilder;
        this.transactionTemplate = transactionTemplate;
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
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

    private void createUser(Consumer<UserBuilder> config) {
        config.accept(userBuilder);
        User user = userBuilder.build();
        saveUserAndAssignRoles(user);
    }
    private void saveUserAndAssignRoles(User user) {
        int userId = userDAO.save(user);
        user.getRoles().forEach(role -> userDAO.assignRoleToUser(userId, role.getId()));
    }
}
