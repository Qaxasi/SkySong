package com.mycompany.SkySong.identity.authentication.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.nio.CharBuffer;
import java.util.Arrays;

public final class RawPassword implements AutoCloseable {
    private final char[] value;
    private RawPassword(final char[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    public static Result<RawPassword> of(final char[] rawValue) {
        if (rawValue == null || rawValue.length == 0) {
            return Result.failure("Password must not be null or empty", ErrorType.VALIDATION_ERROR);
        }
        return Result.success(new RawPassword(rawValue));
    }

    public CharSequence charSequenceView() {
        return CharBuffer.wrap(value);
    }

    public void clear() {
        Arrays.fill(value, '\0');
    }

    @Override
    public void close() {
        clear();
    }

    @Override
    public String toString() {
        return "*****";
    }
}
