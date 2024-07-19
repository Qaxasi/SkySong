package com.mycompany.SkySong.infrastructure.config.user.delete;

import com.mycompany.SkySong.application.user.delete.usecase.UserDeletionHandler;
import com.mycompany.SkySong.domain.user.delete.ports.DeleteUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDeletionConfiguration {

    @Bean
    public UserDeletionHandler userDeletionHandler(DeleteUser deleteUser) {
        return new UserDeletionHandler(deleteUser);
    }
}
