package com.mycompany.SkySong.user.delete.adapters;

import com.mycompany.SkySong.user.delete.domain.ports.TransactionalExecutorPort;

import java.util.function.Supplier;

class TransactionalExecutorAdapter implements TransactionalExecutorPort {
    @Override
    public <T> T execute(Supplier<T> action) {
        return null;
    }
}
