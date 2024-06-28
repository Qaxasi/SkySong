package com.mycompany.SkySong.user.delete.config;

import com.mycompany.SkySong.user.delete.domain.ports.DeleteUser;
import com.mycompany.SkySong.user.delete.domain.service.DeleteUserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteUserConfiguration {
    @Bean
    DeleteUserHandler deleteUserHandler(DeleteUser deleteUser) {
        return new DeleteUserHandler(deleteUser);
    }
}
