package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.service.TestUserCreator;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSaverTest extends BaseIT {

    @Autowired
    private UserSaver userSaver;
    @Autowired
    private UserExistenceChecker userChecker;
    @Autowired
    private TestUserCreator userCreator;
    @Autowired
    private UserRoleChecker roleChecker;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenSaveUser_UserExist() {
        User user = userCreator.createUser("Maks");
        userSaver.saveUser(user);
        assertThat(userChecker.userExist("Maks")).isTrue();
    }

    @Test
    void whenSaveUser_UserHasRole() {
        User user = userCreator.createUser("Maks");
        userSaver.saveUser(user);
        assertThat(roleChecker.hasUserRole("Maks", UserRole.ROLE_USER.name())).isTrue();
    }
}
