package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.UserNotFoundException;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.common.UserIdFetcher;
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
        int userId = 100;
        assertThrows(UserNotFoundException.class, () -> delete.deleteUserById(userId));
    }
}
