package com.mycompany.SkySong.user.delete.application.controller;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.user.delete.domain.service.DeleteUserHandler;
import com.mycompany.SkySong.user.delete.application.exception.NullOrEmptyInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class DeleteUserController {

    private final DeleteUserHandler userDeleter;

    public DeleteUserController(DeleteUserHandler userDeleter) {
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
