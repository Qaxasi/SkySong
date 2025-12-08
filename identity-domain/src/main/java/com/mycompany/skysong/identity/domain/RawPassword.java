package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

import java.nio.CharBuffer;
import java.util.Arrays;

public final class RawPassword implements AutoCloseable {
    private final char[] value;
    private RawPassword(final char[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    public static Result<RawPassword> fromInput(final char[] rawValue) {
        if (rawValue == null || rawValue.length == 0) {
            return Result.failure("Password must not be null or empty", ErrorType.VALIDATION_ERROR);
        }
        return Result.success(new RawPassword(rawValue));
    }

    public CharSequence asCharSequenceView() {
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
