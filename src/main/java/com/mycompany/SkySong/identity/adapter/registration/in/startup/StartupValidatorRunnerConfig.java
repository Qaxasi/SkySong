package com.mycompany.SkySong.identity.adapter.registration.in.startup;

import com.mycompany.SkySong.identity.application.registration.startup.DefaultRoleStartupValidator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupValidatorRunnerConfig {
    private final DefaultRoleStartupValidator validator;

    public StartupValidatorRunnerConfig(final DefaultRoleStartupValidator validator) {
        this.validator = validator;
    }

    @Bean
    public ApplicationRunner startupValidatorRunner() {
        return args -> validator.validate();
    }
}
