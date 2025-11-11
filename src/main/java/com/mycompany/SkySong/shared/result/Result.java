package com.mycompany.SkySong.shared.result;

import com.mycompany.SkySong.shared.error.ErrorType;

import java.util.Map;
import java.util.Optional;
import java.util.function.*;

public sealed interface Result<T> permits Success, Failure {

    static <T> Result<T> success(T data) {
        return new Success<>(data);
    }
    static Result<Unit> success() {
        return new Success<>(Unit.INSTANCE);
    }
    static <T> Result<T> failure(String message, ErrorType errorType) {
        return new Failure<>(errorType, message, Map.of());
    }

    boolean isSuccess();
    default boolean isFailure() {
        return !isSuccess();
    }

    <R> R fold(Function<? super Failure<T>, ? extends R> onFailure,
               Function<? super T, ? extends R> onSuccess);

    default <U> Result<U> map(Function<? super T, ? extends U> mapper) {
        if (this instanceof Success<T> s) {
            return Result.success(mapper.apply(s.data()));
        }
        Failure<?> f = (Failure<?>) this;
        return new Failure<>(f.errorType(), f.message(), f.details());
    }

    default <U> Result<U> flatMap(Function<? super T, ? extends Result<U>> mapper) {
        if (this instanceof Success<T> s) {
            return mapper.apply(s.data());
        }
        Failure<?> f = (Failure<?>) this;
        return new Failure<>(f.errorType(), f.message(), f.details());
    }

    default Result<T> mapError(Function<? super Failure<T>, ? extends Failure<T>> fn) {
        return this.fold(fn, Result::success);
    }

    default Result<T> mapError(String message, ErrorType type) {
        return mapError(f -> f.withMessage(message).withType(type));
    }

    default Optional<T> toOptional() {
        return fold(f -> Optional.empty(), Optional::ofNullable);
    }

    default T orElse(T other) {
        return fold(f -> other, v -> v);
    }

    default T orElseGet(Supplier<T> sup) {
        return fold(f -> sup.get(), v -> v);
    }

    default Result<T> peekFailure(Consumer<? super Failure<T>> c) {
        return fold(f -> {
            c.accept(f); return this;
            },
                s -> this);
    }

    static <A, B, R> Result<R> combine(
            Result<? extends A> ra,
            Result<? extends B> rb,
            java.util.function.BiFunction<? super A, ? super B, ? extends R> f
    ) {
        return ra.flatMap(a -> rb.map(b -> f.apply(a, b)));
    }

    static <A, B, C, R> Result<R> combine(
            Result<? extends A> ra,
            Result<? extends B> rb,
            Result<? extends C> rc,
            TriFunction<? super A, ? super B, ? super C, ? extends R> f) {
        return ra.flatMap(a -> rb.flatMap(b -> rc.map(c -> f.apply(a, b, c))));
    }

    static <A, B, R> Result<R> combineM(Result<A> ra, Result<B> rb, BiFnM<A, B, R> fn) {
        return ra.flatMap(a -> rb.flatMap(b -> fn.apply(a, b)));
    }

    static <A, B, C, R> Result<R> combineM(Result<A> ra, Result<B> rb,
                                           Result<C> rc, TriFnM<A, B, C, R> fn) {
        return ra.flatMap(a -> rb.flatMap(b -> rc.flatMap(c -> fn.apply(a, b, c))));
    }

    static <A, B, C, D, R> Result<R> combineM(Result<A> ra, Result<B> rb, Result<C> rc,
                                              Result<D> rd, QuadFnM<A,B, C, D, R> fn) {
        return ra.flatMap(a -> rb.flatMap(b -> rc.flatMap(c -> rd.flatMap(d -> fn.apply(a, b, c, d)))));
    }
}