package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserFacade {

    private final DeleteUserHandler deleteUser;

    public DeleteUserFacade(DeleteUserHandler deleteUser) {
        this.deleteUser = deleteUser;
    }

    public ApiResponse deleteUser(int userId) {
        return deleteUser.deleteUser(userId);
    }
}
