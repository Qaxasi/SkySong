package com.mycompany.SkySong.user;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.testsupport.auth.common.TestUserCreator;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.security.RoleChecker;
import com.mycompany.SkySong.testsupport.auth.security.SessionChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

class DeleteUserTest extends BaseIT {

    @Autowired
    private DeleteUser delete;
    @Autowired
    private UserExistenceChecker userChecker;
    @Autowired
    private SessionChecker sessionChecker;
    @Autowired
    private RoleChecker roleChecker;
    @Autowired
    private UserFixture userCreator;

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
    void whenUserNotExist_ThrowException() {
        assertThrows(UserNotFoundException.class, () -> delete.deleteUserById(1));
    }

    @Test
    void whenUserExist_DeleteUser() {
        userCreator.createUserWithId(1);
        delete.deleteUserById(1);
        assertThat(userChecker.userExist(1)).isFalse();
    }

    @Test
    void whenUserExist_DeleteUserSessions() {
        userCreator.createUserWithId(1);
        delete.deleteUserById(1);
        assertThat(sessionChecker.userHasActiveSession(1)).isFalse();
    }

    @Test
    void whenUserExist_DeleteUserRoles() {
        userCreator.createUserWithId(1);
        delete.deleteUserById(1);
        assertThat(roleChecker.userHasRoles(1)).isFalse();
    }
}
