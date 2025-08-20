package com.mycompany.SkySong.identity.registration.adapter.in;

import com.mycompany.SkySong.identity.registration.application.dto.UserRegistrationData;
import com.mycompany.SkySong.identity.registration.application.service.UserRegistration;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserRegistrationController {

    private final UserRegistration userRegistration;
    private final RegistrationRequestMapper mapper;

    public UserRegistrationController(final UserRegistration userRegistration,
                                      final RegistrationRequestMapper mapper) {
        this.userRegistration = userRegistration;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@Valid @RequestBody final RegistrationRequest request) {
        final UserRegistrationData dto = mapper.toDto(request);

        return userRegistration.register(dto)
                .fold(
                        error -> ResponseEntity
                                .status(error.errorType().getHttpStatus())
                                .body(error.toErrorResponse()),

                        success -> ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(new SuccessResponse("Your registration was successful!"))
                );
    }
}
