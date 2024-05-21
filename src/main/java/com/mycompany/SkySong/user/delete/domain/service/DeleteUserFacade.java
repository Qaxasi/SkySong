package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserFacade {

    private final DeleteUser deleteUser;

    public DeleteUserFacade(DeleteUser deleteUser) {
        this.deleteUser = deleteUser;
    }

    public ApiResponse deleteUser(int userId) {
        return deleteUser.deleteUserById(userId);
    }
}
