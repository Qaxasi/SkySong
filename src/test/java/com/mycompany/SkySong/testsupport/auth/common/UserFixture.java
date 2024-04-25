package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.user.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Component
public class UserFixture {

    private final UserBuilder userBuilder;
    private final SessionBuilder sessionBuilder;

    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;

    public UserFixture(UserBuilder userBuilder,
                       SessionBuilder sessionBuilder,
                       UserDAO userDAO,
                       SessionDAO sessionDAO) {
        this.userBuilder = userBuilder;
        this.sessionBuilder = sessionBuilder;
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
    }


    private void createUser(Consumer<UserBuilder> config) {
        config.accept(userBuilder);
        User user = userBuilder.build();
        saveUserAndAssignRoles(user);
    }


    private void createSession(Consumer<SessionBuilder> config) {
        config.accept(sessionBuilder);
        Session session = sessionBuilder.build();
        saveSession(session);
    }

    private void saveUserAndAssignRoles(User user) {
        int userId = userDAO.save(user);
        user.getRoles().forEach(role -> userDAO.assignRoleToUser(userId, role.getId()));
    }
}
