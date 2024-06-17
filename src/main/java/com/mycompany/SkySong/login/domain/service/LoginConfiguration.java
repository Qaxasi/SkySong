package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.login.domain.ports.LoginSessionRepository;
import com.mycompany.SkySong.login.domain.ports.LoginUserRepository;
import com.mycompany.SkySong.login.domain.ports.UserAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginConfiguration {

    @Bean
    LoginHandler loginHandler(UserAuthentication auth,
                              LoginSessionRepository sessionRepository,
                              LoginUserRepository userRepository) {
        SessionTokenGenerator tokenGenerator = new SessionTokenGenerator();
        UserSessionCreator sessionCreator = new UserSessionCreator(sessionRepository, tokenGenerator);
        return new LoginHandler(sessionCreator, auth, userRepository);
    }
}
