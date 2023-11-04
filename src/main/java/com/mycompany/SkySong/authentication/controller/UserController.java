package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;
import com.mycompany.SkySong.authentication.service.DeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final DeleteService deleteService;

    public UserController(DeleteService deleteService) {
        this.deleteService = deleteService;
    }
    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<DeleteResponse> delete(@PathVariable(required = false) Long userId) {
        if (userId == null) {
            throw new NullOrEmptyInputException("User ID cannot be empty");
        }
        DeleteResponse deleteResponse = deleteService.deleteUser(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
