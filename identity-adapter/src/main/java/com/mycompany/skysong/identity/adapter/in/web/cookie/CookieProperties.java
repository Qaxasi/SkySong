package com.mycompany.skysong.identity.adapter.in.web.cookie;

import jakarta.validation.constraints.NotBlank;

record CookieProperties(
        @NotBlank String name,
        @NotBlank String path,
        boolean httpOnly,
        boolean secure,
        @NotBlank String sameSite
) {}