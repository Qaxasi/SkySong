package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
class JdbiConfig {
    @Bean
    Jdbi config(DataSource dataSource) {
        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(dataSource);

        Jdbi jdbi = Jdbi.create(dataSourceProxy);
        jdbi.installPlugin(new SqlObjectPlugin());

        return jdbi;
    }

    @Bean
    UserIdentityDAO dao(Jdbi jdbi) {
        return jdbi.onDemand(UserIdentityDAO.class);
    }
}
