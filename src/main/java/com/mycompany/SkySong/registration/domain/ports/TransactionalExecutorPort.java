package com.mycompany.SkySong.registration.domain.ports;

import java.util.function.Supplier;

public interface TransactionalExecutorPort {
    <T> T execute(Supplier<T> action);
}
