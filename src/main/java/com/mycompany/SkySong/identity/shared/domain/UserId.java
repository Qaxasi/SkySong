package com.mycompany.SkySong.identity.shared.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

public record UserId(int value) {
    public UserId {
        if (value <= 0) {
            throw new IllegalArgumentException("UserId must be positive");
        }
    }

    public int asInt() {
        return value;
    }

    public static Result<UserId> of(int value) {
        return (value <= 0) ?
                Result.failure("UserId must be positive", ErrorType.INVARIANT_VIOLATION)
                : Result.success(new UserId(value));
    }
}
