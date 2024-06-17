package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.user.delete.domain.ports.DeleteUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteUserConfiguration {

    @Bean
    DeleteUserHandler deleteUserHandler(DeleteUser deleteUser) {
        return new DeleteUserHandler(deleteUser);
    }
}
