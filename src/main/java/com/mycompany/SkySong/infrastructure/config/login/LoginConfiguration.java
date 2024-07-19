package com.mycompany.SkySong.infrastructure.config.login;

import com.mycompany.SkySong.application.login.usecase.LoginHandler;
import com.mycompany.SkySong.domain.login.ports.LoginSessionRepository;
import com.mycompany.SkySong.domain.login.ports.LoginUserRepository;
import com.mycompany.SkySong.domain.login.ports.UserAuthentication;
import com.mycompany.SkySong.domain.login.service.UserSessionCreator;
import com.mycompany.SkySong.domain.login.service.UserSessionCreatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfiguration {

    @Bean
    public LoginHandler loginHandler(UserAuthentication auth,
                              UserSessionCreator sessionCreator,
                              LoginUserRepository userRepository) {
        return new LoginHandler(sessionCreator, auth, userRepository);
    }
    @Bean
    public UserSessionCreator sessionCreator(LoginSessionRepository sessionRepository) {
        UserSessionCreatorFactory factory = new UserSessionCreatorFactory();
        return factory.createUserSessionCreator(sessionRepository);
    }
}
