package com.mycompany.SkySong.identity.shared.domain;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public final class UserTag {
    public static final int BYTES_LENGTH = 16;
    public static final int BASE64URL_LENGTH = 22;
    private static final Base64.Encoder B64URL_ENC = Base64.getUrlEncoder().withoutPadding();
    private static final Pattern FORMAT =  Pattern.compile("^[A-Za-z0-9_-]{" + BASE64URL_LENGTH + "}$");

    private final String value;
    private UserTag(String base64url) {
        this.value = base64url;
    }

    public static Result<UserTag> fromBytes(byte[] bytes) {
        if (bytes == null || bytes.length != BYTES_LENGTH) {
            return Result.failure("Invalid user tag length", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new UserTag(B64URL_ENC.encodeToString(bytes)));
    }

    public static Result<UserTag> of(String s) {
        if (s == null || !FORMAT.matcher(s).matches()) {
            return Result.failure("Invalid user tag", ErrorType.INVARIANT_VIOLATION);
        }
        return Result.success(new UserTag(s));
    }

    public String asBase64Url() {
        return value;
    }
    @Override public boolean equals(Object o) { return (o instanceof UserTag t) && Objects.equals(this.value, t.value); }
    @Override public int hashCode() { return value.hashCode(); }
    @Override public String toString() { return value; }
}
