package com.mycompany.skysong.identity.domain;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;

import java.util.regex.Pattern;

public final class PasswordPolicy {
    private static final Pattern STRONG =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    private PasswordPolicy() {}

    public static Result<Unit> validateForRegistration(final RawPassword password) {
        if (password == null) {
            return Result.failure("Password must not be null", ErrorType.VALIDATION_ERROR);
        }

        final CharSequence raw = password.asCharSequenceView();

        if (raw.length() == 0) {
            return Result.failure("Password must not be empty", ErrorType.VALIDATION_ERROR);
        }

        if (!STRONG.matcher(raw).matches()) {
            return Result.failure("Password does not meet the complexity requirements", ErrorType.VALIDATION_ERROR);
        }
        return Result.success();
    }
}
