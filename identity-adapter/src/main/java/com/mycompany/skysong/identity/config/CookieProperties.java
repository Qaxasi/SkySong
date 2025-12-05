package com.mycompany.skysong.identity.config;

import jakarta.validation.constraints.NotBlank;

public record CookieProperties(
        @NotBlank String name,
        @NotBlank String path,
        boolean httpOnly,
        boolean secure,
        @NotBlank String sameSite
) {}