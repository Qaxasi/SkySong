package com.mycompany.SkySong.infrastructure.user.delete.config;

import com.mycompany.SkySong.domain.user.delete.ports.DeleteUser;
import com.mycompany.SkySong.domain.user.delete.service.DeleteUserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteUserConfiguration {
    @Bean
    DeleteUserHandler deleteUserHandler(DeleteUser deleteUser) {
        return new DeleteUserHandler(deleteUser);
    }
}
