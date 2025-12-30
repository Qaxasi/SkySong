package com.mycompany.skysong.app.config.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
class TransactionTemplateConfig {
    @Bean
    TransactionTemplate transactionTemplate(final PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
