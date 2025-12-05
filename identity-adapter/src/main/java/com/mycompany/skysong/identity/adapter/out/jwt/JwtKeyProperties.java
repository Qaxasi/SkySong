package com.mycompany.skysong.identity.adapter.out.jwt;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "identity.authentication.jwt")
public record JwtKeyProperties(
        @NotBlank String secretKey) {
}
