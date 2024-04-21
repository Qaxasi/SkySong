package com.mycompany.SkySong.user;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteUserServiceTest extends BaseIT {

    @Autowired
    private DeleteUserService deleter;
    @Autowired
    private UserFixture userFixture;

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
        userFixture.createUserWithId(1);
        ApiResponse response = deleter.deleteUser(1);
        String expectedMessage = String.format("User with ID %d deleted successfully.", 1);
        assertThat(response.message()).isEqualTo(expectedMessage);
    }
}
