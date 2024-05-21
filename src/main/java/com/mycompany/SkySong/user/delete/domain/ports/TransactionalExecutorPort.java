package com.mycompany.SkySong.user.delete.domain.ports;

import java.util.function.Supplier;

public interface TransactionalExecutorPort {
    <T> T execute(Supplier<T> action);
}
