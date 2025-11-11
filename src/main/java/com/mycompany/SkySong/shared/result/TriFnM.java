package com.mycompany.SkySong.shared.result;

@FunctionalInterface
public interface TriFnM<A, B, C, R> {
    Result<R> apply(A a, B b, C c);
}
