package com.mycompany.SkySong.user;

import com.mycompany.SkySong.testsupport.auth.common.TestUserCreator;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteUserServiceTest extends BaseIT {

    @Autowired
    private DeleteUserService deleter;
    @Autowired
    private TestUserCreator userCreator;

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
    void whenUserDeleted_ReturnMessage() {
        int userId = 1;
        ApiResponse response = deleter.deleteUser(userId);
        String expectedMessage = String.format("User with ID %d deleted successfully.", userId);
        assertThat(response.message()).isEqualTo(expectedMessage);
    }
}
