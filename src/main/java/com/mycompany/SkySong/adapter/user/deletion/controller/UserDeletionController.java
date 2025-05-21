package com.mycompany.SkySong.adapter.user.deletion.controller;

import com.mycompany.SkySong.application.user.delete.usecase.UserDeletionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserDeletionController {

    private final UserDeletionUseCase userDeletion;

    public UserDeletionController(final UserDeletionUseCase userDeletion) {
        this.userDeletion = userDeletion;
    }

    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<Object> delete(@PathVariable(required = false) final Integer userId) {
        return new ResponseEntity<>(userDeletion.delete(userId), HttpStatus.OK);
    }
}
