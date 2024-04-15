package com.mycompany.SkySong.user;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class DeleteUserController {

    private final DeleteUserService delete;

    private final ApplicationMessageLoader message;

    public DeleteUserController(DeleteUserService delete,
                                ApplicationMessageLoader message) {
        this.delete = delete;
        this.message = message;
    }
    
    @DeleteMapping({"/","/{userId}"})
    public ResponseEntity<ApiResponse> delete(@PathVariable(required = false) Integer userId) {
        if (userId == null) {
            String errorMessage = message.getMessage("user.id.required");
            throw new NullOrEmptyInputException(errorMessage);
        }
        ApiResponse deleteResponse = delete.deleteUser(userId);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }
}
