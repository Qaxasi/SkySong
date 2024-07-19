package com.mycompany.SkySong.adapter.user.delete.controller;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.adapter.user.delete.controller.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.application.user.delete.usecase.UserDeletionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class DeleteUserController {

    private final UserDeletionHandler userDeleter;

    public DeleteUserController(UserDeletionHandler userDeleter) {
        this.userDeleter = userDeleter;
    }

    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<ApiResponse> delete(@PathVariable(required = false) Integer userId) {
        if (userId == null) {
            throw new NullOrEmptyInputException("User ID is required and cannot be empty.");
        }
        ApiResponse deleteResponse = userDeleter.delete(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
