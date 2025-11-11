package com.mycompany.SkySong.shared.result;

public interface TriFnM<A, B, C, R> {
    Result<R> apply(A a, B b, C c);
}
