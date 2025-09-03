package com.mycompany.SkySong.identity.authentication.adapter.out.shared.token;

public class OpaqueRefreshTokenFormat {
    public static final int USER_KEY_LENGTH = 16;
    public static final int RANDOM_PART_LENGTH = 32;
    public static final int RAW_TOKEN_LENGTH = USER_KEY_LENGTH + RANDOM_PART_LENGTH;

    private OpaqueRefreshTokenFormat() {}
}
