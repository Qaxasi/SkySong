package com.mycompany.SkySong.shared.result;

import com.mycompany.SkySong.shared.error.ErrorType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public record Result<T>(
        T data,
        String errorMessage,
        boolean isSuccessful,
        ErrorType errorType) {

    public static <T> Result<T> success(T data) {
        return new Result<>(data, null, true, null);
    }

    public static Result<Void> success() {
        return new Result<>(null, null, true, null);
    }

    public static <T> Result<T> failure(String errorMessage, ErrorType errorType) {
        return new Result<>(null, errorMessage, false, errorType);
    }
    public <U> Result<U> mapFailure() {
        if (isSuccess()) throw new IllegalStateException("Cannot map success as failure");
        return Result.failure(this.errorMessage(), this.errorType());
    }

    public boolean isSuccess() {
        return isSuccessful;
    }

    public boolean isFailure() {
        return !isSuccessful;
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        if (this.isSuccess()) {
            return mapper.apply(this.data);
        } else {
            return Result.failure(this.errorMessage(), this.errorType());
        }
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        if (this.isSuccess()) {
            return Result.success(mapper.apply(this.data));
        } else {
            return Result.failure(this.errorMessage(), this.errorType());
        }
    }
    public Result<T> onFailure(Consumer<Result<T>> consumer) {
        if (isFailure()) {
            consumer.accept(this);
        }
        return this;
    }
    public Result<T> onFailure(BiConsumer<ErrorType, String> consumer) {
        if (isFailure()) {
            consumer.accept(this.errorType, this.errorMessage);
        }
        return this;
    }

    public Result<T> onSuccess(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(data());
        }
        return this;
    }

    public Result<T> onSuccess(Runnable action) {
        if (isSuccess()) {
            action.run();
        }
        return this;
    }

    public <R> R fold(Function<Result<T>, R> onFailure, Function<T, R> onSuccess) {
        if (isSuccess()) {
            return onSuccess.apply(this.data);
        } else {
            return onFailure.apply(this);
        }
    }

    public Result<T> mapError(BiFunction<ErrorType, String, Result<T>> fn) {
        if (isSuccess()) return this;
        return fn.apply(this.errorType, this.errorMessage);
    }

    public Optional<T> toOptional() {
        return isFailure() ? Optional.empty() : Optional.ofNullable(data);
    }

    public T getOrThrow() {
        if (isFailure()) {
            throw new IllegalStateException("Result is failure");
        }
        return data;
    }

    public <U> Result<U> propagateFailure() {
        if (isFailure()) {
            return Result.failure(errorMessage(), errorType());
        }
        throw new IllegalStateException("Cannot propagate success");
    }

    public T get() {
        return data;
    }
}