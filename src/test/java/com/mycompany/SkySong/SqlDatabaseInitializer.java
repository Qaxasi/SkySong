package com.mycompany.SkySong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
@Component
public class SqlDatabaseInitializer {

    @Autowired
    private DataSource dataSource;

    public void setup(String scriptPath) throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
        }
    }
}
