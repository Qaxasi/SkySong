package com.mycompany.SkySong.identity.a.adapter.in.web.authentication;

import com.mycompany.SkySong.identity.a.domain.RawPassword;
import com.mycompany.SkySong.shared.result.Result;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Function;

@Component
public class RawPasswordGuard {
    public <R> Result<R> useAndZeroize(final char[] source,
                                       final Function<RawPassword, Result<R>> function) {
        try {
            return RawPassword.fromInput(source)
                    .flatMap(pwd -> {
                        try(pwd) {
                            return function.apply(pwd);
                        }
                    });

        } finally {
            Arrays.fill(source, '\0');
        }
    }
}
