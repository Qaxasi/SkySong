package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.registration.domain.ports.TransactionalExecutorPort;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

class TransactionalExecutorAdapter implements TransactionalExecutorPort {

    private final TransactionTemplate transactionTemplate;

    TransactionalExecutorAdapter(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public <T> T execute(Supplier<T> action) {
        return null;
    }
}
