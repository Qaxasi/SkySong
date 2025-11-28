package com.mycompany.skysong.core.result;

@FunctionalInterface
public interface BiFnM<A, B, R> {
    Result<R> apply(A a, B b);
}
