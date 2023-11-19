package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.service.DeleteUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class DeleteUserController {
    private final DeleteUserService deleteUserService;
    public DeleteUserController(DeleteUserService deleteUserService) {
        this.deleteUserService = deleteUserService;
    }
    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<ApiResponse> delete(@PathVariable(required = false) Long userId) {
        if (userId == null) {
            throw new NullOrEmptyInputException("User ID is required and cannot be empty.");
        }
        ApiResponse deleteResponse = deleteUserService.deleteUser(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
