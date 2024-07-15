package com.mycompany.SkySong.application.user.delete.usecase;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.domain.user.delete.ports.DeleteUser;

public class UserDeletionHandler {

    private final DeleteUser deleteUser;

    public UserDeletionHandler(DeleteUser deleteUser) {
        this.deleteUser = deleteUser;
    }

    public ApiResponse delete(int userId) {
        deleteUser.deleteEverythingById(userId);
        return new ApiResponse(String.format("User with ID %d deleted successfully.", userId));
    }
}