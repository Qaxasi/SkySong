package com.mycompany.SkySong.user;

import com.mycompany.SkySong.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class DeleteUserController {

    private final DeleteUserService delete;

    public DeleteUserController(DeleteUserService delete) {
        this.delete = delete;
    }
    
    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<ApiResponse> delete(@PathVariable(required = false) Integer userId) {
        if (userId == null) {
            throw new NullOrEmptyInputException("User ID is required and cannot be empty.");
        }
        ApiResponse deleteResponse = delete.deleteUser(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
