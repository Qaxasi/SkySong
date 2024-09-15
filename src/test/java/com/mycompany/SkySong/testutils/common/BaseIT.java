package com.mycompany.SkySong.testutils.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class BaseIT {

    private static final MySQLContainer<?> container;
    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    static {
        container = new MySQLContainer<>("mysql:8.2.0")
                .withInitScript("data_sql/create-tables.sql")
                .withReuse(true);

        container.start();

        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }

    @BeforeEach
    public void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    public void cleanUp() {
        cleaner.clean();
    }
}
