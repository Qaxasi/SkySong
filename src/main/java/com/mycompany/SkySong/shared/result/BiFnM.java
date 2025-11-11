package com.mycompany.SkySong.shared.result;

public interface BiFnM<A, B, R> {
    Result<R> apply(A a, B b);
}
