package com.mycompany.SkySong.testutils.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SqlDatabaseCleaner {

    private final JdbcTemplate jdbcTemplate;
    private final List<String> tablesToClean = Arrays.asList("user_roles", "sessions", "users", "roles");

    public SqlDatabaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void clean() {
        tablesToClean.forEach(table -> {
            jdbcTemplate.update("DELETE FROM " + table);
            jdbcTemplate.update("ALTER TABLE " + table + " AUTO_INCREMENT = 1");
        });
    }
}
