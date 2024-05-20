package com.mycompany.SkySong.user.delete.domain.service;

import com.mycompany.SkySong.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeleteUserHandler {

    private final DeleteUser delete;

    public DeleteUserHandler(DeleteUser deletionManager) {
        this.delete = deletionManager;
    }

    public ApiResponse deleteUser(int userId) {
        delete.deleteUserById(userId);
        log.info("User with ID {} deleted successfully", userId);
        return new ApiResponse(String.format("User with ID %d deleted successfully.", userId));
    }
}

