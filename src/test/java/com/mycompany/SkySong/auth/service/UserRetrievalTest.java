package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class UserRetrievalTest extends BaseIT {

    @Autowired
    private SqlDatabaseInitializer initializer;

    @Autowired
    private SqlDatabaseCleaner cleaner;

    @Autowired
    private UserRetrieval retrieval;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenUserExist_ReturnUser() {
        User user = retrieval.findByAuthUsername("Mark");
        assertThat(user).isNotNull();
    }

    @Test
    void whenUserRetrieved_UsernameMatches() {
        User user = retrieval.findByAuthUsername("Mark");
        assertThat(user.getUsername()).isEqualTo("Mark");
    }

    @Test
    void whenUserNotExist_ThrowException() {
        assertThrows(UsernameNotFoundException.class, () -> retrieval.findByAuthUsername("Emil"));
    }
}
