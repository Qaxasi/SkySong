package com.mycompany.SkySong.registration.domain.ports;

import java.util.function.Supplier;

public interface TransactionalExecutor {
    <T> T execute(Supplier<T> action);
}
