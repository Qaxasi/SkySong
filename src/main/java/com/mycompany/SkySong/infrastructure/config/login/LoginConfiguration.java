//package com.mycompany.SkySong.infrastructure.config.login;
//
//import com.mycompany.SkySong.domain.login.ports.LoginSessionRepository;
//import com.mycompany.SkySong.domain.login.ports.LoginUserRepository;
//import com.mycompany.SkySong.domain.login.ports.UserAuthentication;
//import com.mycompany.SkySong.application.login.usecase.LoginHandler;
//import com.mycompany.SkySong.domain.login.service.LoginHandlerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//class LoginConfiguration {
//    @Bean
//    LoginHandler loginHandler(LoginSessionRepository sessionRepository,
//                              UserAuthentication userAuth,
//                              LoginUserRepository userRepository) {
//        LoginHandlerFactory factory = new LoginHandlerFactory();
//        return factory.createLoginHandler(sessionRepository, userAuth, userRepository);
//    }
//}
