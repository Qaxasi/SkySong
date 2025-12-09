package com.mycompany.skysong.app.config.identity;

import com.mycompany.skysong.app.config.identity.properties.AccessTokenProperties;
import com.mycompany.skysong.identity.adapter.out.security.jwt.JwtTokenGenerator;
import com.mycompany.skysong.identity.application.authentication.port.AccessTokenGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.time.Clock;

@Configuration
class AccessTokenGeneratorConfig {
    @Bean
    public AccessTokenGenerator accessTokenGenerator(@Qualifier("accessTokenSigningKey") final SecretKey secretKey,
                                                     final AccessTokenProperties properties,
                                                     final Clock clock) {
        return new JwtTokenGenerator(
                secretKey,
                properties.ttl(),
                clock);
    }
}
