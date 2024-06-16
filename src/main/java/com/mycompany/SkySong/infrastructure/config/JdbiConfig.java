package com.mycompany.SkySong.infrastructure.config;

import com.mycompany.SkySong.common.dao.RoleDAO;
import com.mycompany.SkySong.common.dao.SessionDAO;
import com.mycompany.SkySong.common.dao.UserDAO;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class JdbiConfig {

    @Bean
    public Jdbi config(DataSource dataSource) {
        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(dataSource);

        Jdbi jdbi = Jdbi.create(dataSourceProxy);
        jdbi.installPlugin(new SqlObjectPlugin());

        return jdbi;
    }

    @Bean
    public SessionDAO sessionDAO(Jdbi jdbi) {
        return jdbi.onDemand(SessionDAO.class);
    }

    @Bean
    public UserDAO userDAO(Jdbi jdbi) {
        return jdbi.onDemand(UserDAO.class);
    }
    @Bean
    public RoleDAO roleDAO(Jdbi jdbi) {
        return jdbi.onDemand(RoleDAO.class);
    }

}
