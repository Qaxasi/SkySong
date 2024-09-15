package com.mycompany.SkySong.testutils.common;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class SqlDatabaseInitializer {
    
    private final DataSource dataSource;

    public SqlDatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setup(String scriptPath) throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
        }
    }
}
