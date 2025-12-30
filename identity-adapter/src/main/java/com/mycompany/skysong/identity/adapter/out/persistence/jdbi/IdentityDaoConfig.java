package com.mycompany.skysong.identity.adapter.out.persistence.jdbi;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdentityDaoConfig {
    @Bean
    UserIdentityDAO dao(final Jdbi jdbi) {
        return jdbi.onDemand(UserIdentityDAO.class);
    }
}
