package com.mycompany.skysong.core.result;

import java.util.function.Function;

public record Success<T>(T data) implements Result<T> {

    @Override public boolean isSuccess() {
        return true;
    }

    @Override
    public <R> R fold(Function<? super Failure<T>, ? extends R> onFailure,
                      Function<? super T, ? extends R> onSuccess) {
        return onSuccess.apply(data);
    }
}