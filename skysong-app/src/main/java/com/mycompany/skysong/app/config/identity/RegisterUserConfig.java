package com.mycompany.skysong.app.config.identity;

import com.mycompany.skysong.identity.application.user.registration.port.PasswordHasher;
import com.mycompany.skysong.identity.application.user.registration.port.UserStore;
import com.mycompany.skysong.identity.application.user.registration.port.UserTagGenerator;
import com.mycompany.skysong.identity.application.user.registration.port.UserUniquenessChecker;
import com.mycompany.skysong.identity.application.user.registration.service.RegisterUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegisterUserConfig {

    @Bean
    RegisterUser register(final UserUniquenessChecker uniquenessChecker,
                          final PasswordHasher passwordHasher,
                          final UserTagGenerator userTagGenerator,
                          final UserStore userStore) {
        return new RegisterUser(
                uniquenessChecker,
                passwordHasher,
                userTagGenerator,
                userStore);
    }
}
