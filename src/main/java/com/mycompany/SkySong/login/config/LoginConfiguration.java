package com.mycompany.SkySong.login.config;

import com.mycompany.SkySong.login.domain.ports.LoginSessionRepository;
import com.mycompany.SkySong.login.domain.ports.LoginUserRepository;
import com.mycompany.SkySong.login.domain.ports.UserAuthentication;
import com.mycompany.SkySong.login.domain.service.LoginHandler;
import com.mycompany.SkySong.login.domain.service.LoginHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginConfiguration {
    @Bean
    LoginHandler loginHandler(LoginSessionRepository sessionRepository,
                              UserAuthentication userAuth,
                              LoginUserRepository userRepository) {
        LoginHandlerFactory factory = new LoginHandlerFactory();
        return factory.createLoginHandler(sessionRepository, userAuth, userRepository);
    }
}
