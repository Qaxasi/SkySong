package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.user.delete.domain.ports.DeleteUser;

public class DeleteUserHandler {

    private final DeleteUser deleteUser;

    public DeleteUserHandler(DeleteUser deleteUser) {
        this.deleteUser = deleteUser;
    }

    public ApiResponse delete(int userId) {
        deleteUser.deleteEverythingById(userId);
        return new ApiResponse(String.format("User with ID %d deleted successfully.", userId));
    }
}