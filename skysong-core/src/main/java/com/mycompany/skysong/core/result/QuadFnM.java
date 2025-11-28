package com.mycompany.skysong.core.result;

@FunctionalInterface
public interface QuadFnM<A, B, C, D, R> {
    Result<R> apply(A a, B b, C c, D d);
}
