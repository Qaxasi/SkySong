package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAuthProcessorTest extends BaseIT {

    @Autowired
    private UserAuthProcessor authProcessor;

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
    void whenFetchUser_UserExist() {
        User user = authProcessor.fetchUserByAuthentication(LoginRequests.LOGIN("User"));
        assertThat(user).isNotNull();
    }

    @Test
    void whenFetchUser_UsernameIsCorrect() {
        User user = authProcessor.fetchUserByAuthentication(LoginRequests.LOGIN("User"));
        assertThat(user.getUsername()).isEqualTo("User");
    }
}
