package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserService {

    private final DeleteUserHandler deleteUser;

    public DeleteUserService(DeleteUserHandler deleteUser) {
        this.deleteUser = deleteUser;
    }

    public ApiResponse deleteUser(int userId) {
        return deleteUser.delete(userId);
    }
}
