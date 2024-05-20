package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.registration.domain.ports.TransactionalExecutorPort;

import java.util.function.Supplier;

public class TransactionalExecutorAdapter implements TransactionalExecutorPort {
    @Override
    public <T> T execute(Supplier<T> action) {
        return null;
    }
}
