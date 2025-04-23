package com.mycompany.SkySong.adapter.identity.deletion.controller;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.user.delete.usecase.UserDeletionHandler;
import com.mycompany.SkySong.shared.error.ErrorResponse;
import com.mycompany.SkySong.shared.error.ErrorType;
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
    public ResponseEntity<Object> delete(@PathVariable(required = false) Integer userId) {
        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse(
                            "User ID is required and cannot be empty.",
                            ErrorType.USER_REQUEST_INVALID.name(),
                            ErrorType.USER_REQUEST_INVALID.getHttpStatus().value()));

        }

        ApiResponse deleteResponse = userDeleter.delete(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
