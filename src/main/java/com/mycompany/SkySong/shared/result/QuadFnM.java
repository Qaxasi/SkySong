package com.mycompany.SkySong.shared.result;

public interface QuadFnM<A, B, C, D, R> {
    Result<R> apply(A a, B b, C c, D d);
}
