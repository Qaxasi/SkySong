package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.common.UserIdFetcher;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteUserServiceTest extends BaseIT {

    @Autowired
    private DeleteUserService deleter;
    @Autowired
    private UserIdFetcher idFetcher;
    @Autowired
    private UserExistenceChecker userChecker;

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
    void whenUserIdExist_DeleteUser() {
        long userId = idFetcher.fetchByUsername("Mark");
        deleter.deleteUser(userId);
        assertThat(userChecker.userExist("Mark")).isFalse();
    }

    @Test
    void whenUserDeleted_ReturnMessage() {
        long userId = idFetcher.fetchByUsername("Mark");
        ApiResponse response = deleter.deleteUser(userId);
        String expectedMessage = String.format("User with ID %d deleted successfully.", userId);
        assertThat(response.message()).isEqualTo(expectedMessage);
    }

    @Test
    void wheUserIdNotExist_ThrowException() {
        long userId = 100L;
        assertThrows(UserNotFoundException.class, () -> deleter.deleteUser(userId));
    }

    @Test
    void whenUserIdNotExist_ReturnMessage() {
        long userId = 100L;
        Exception exception = assertThrows(UserNotFoundException.class, () -> deleter.deleteUser(userId));
        String expectedMessage = String.format("User with ID %d does not exist.", userId);
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
