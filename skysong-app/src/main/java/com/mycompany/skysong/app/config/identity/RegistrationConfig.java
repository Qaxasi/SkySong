package com.mycompany.skysong.app.config.identity;

import com.mycompany.skysong.identity.application.registration.port.PasswordHasher;
import com.mycompany.skysong.identity.application.registration.port.UserStore;
import com.mycompany.skysong.identity.application.registration.port.UserTagGenerator;
import com.mycompany.skysong.identity.application.registration.port.UserUniquenessChecker;
import com.mycompany.skysong.identity.application.registration.service.UserRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistrationConfig {

    @Bean
    public UserRegistration userRegistration(final UserUniquenessChecker uniquenessChecker,
                                             final PasswordHasher passwordHasher,
                                             final UserTagGenerator userTagGenerator,
                                             final UserStore userStore) {
        return new UserRegistration(
                uniquenessChecker,
                passwordHasher,
                userTagGenerator,
                userStore);
    }
}
