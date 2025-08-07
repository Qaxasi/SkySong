package com.mycompany.SkySong.shared;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record CookieProperties(
        @NotBlank String name,
        @NotBlank String path,
        @Min(1) int maxAge,
        boolean httpOnly,
        boolean secure,
        @NotBlank String sameSite
) {}