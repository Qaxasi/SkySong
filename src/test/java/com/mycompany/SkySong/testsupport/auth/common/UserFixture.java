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
}
