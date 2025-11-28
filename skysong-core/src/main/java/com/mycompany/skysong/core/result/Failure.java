package com.mycompany.skysong.core.result;

import com.mycompany.skysong.core.error.ErrorType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record Failure<T>(ErrorType errorType, String message,
                         Map<String, Object> details) implements Result<T> {

    public Failure {
        details = (details == null) ? Map.of() : Map.copyOf(details);
    }

    @Override public boolean isSuccess() {
        return false;
    }

    @Override
    public <R> R fold(Function<? super Failure<T>, ? extends R> onFailure,
                      Function<? super T, ? extends R> onSuccess) {
        return onFailure.apply(this);
    }

    public Failure<T> withDetail(String key, Object value) {
        var map = new HashMap<>(details);
        map.put(key, value);
        return new Failure<>(errorType, message, Map.copyOf(map));
    }

    public Failure<T> withDetails(Map<String, ?> extra) {
        if (extra == null || extra.isEmpty()) return this;
        var map = new HashMap<>(details);
        map.putAll(extra);
        return new Failure<>(errorType, message, Map.copyOf(map));
    }

    public Failure<T> withType(ErrorType newType) {
        return new Failure<>(newType, message, details);
    }

    public Failure<T> withMessage(String newMessage) {
        return new Failure<>(errorType, newMessage, details);
    }
}