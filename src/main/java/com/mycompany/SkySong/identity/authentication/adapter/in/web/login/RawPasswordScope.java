package com.mycompany.SkySong.identity.authentication.adapter.in.web.login;

import com.mycompany.SkySong.identity.authentication.domain.RawPassword;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class RawPasswordScope {

    public <R> Result<R> useAndZeroize(final char[] source,
                                       final Function<RawPassword, Result<R>> function) {
        final Result<RawPassword> passwordRes = RawPassword.of(source);
        try {
            if (passwordRes.isFailure()) {
                return passwordRes.propagateFailure();
            }
            try(final RawPassword password = passwordRes.get()) {
                return function.apply(password);
            }
        } finally {
            Arrays.fill(source, '\0');
        }
    }
}
