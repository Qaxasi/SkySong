package com.mycompany.SkySong.identity.adapter.registration.in.web;

import com.mycompany.SkySong.identity.adapter.registration.in.web.mapper.RegistrationRequestMapper;
import com.mycompany.SkySong.identity.adapter.registration.in.web.dto.RegistrationRequest;
import com.mycompany.SkySong.identity.application.registration.dto.UserRegistrationInput;
import com.mycompany.SkySong.identity.application.registration.service.UserRegistration;
import com.mycompany.SkySong.shared.response.SuccessResponse;
import com.mycompany.SkySong.shared.response.BaseResponse;
import com.mycompany.SkySong.shared.result.Result;
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
        final UserRegistrationInput dto = mapper.toDto(request);
        final Result<SuccessResponse> result = userRegistration.execute(dto);

        if (result.isFailure()) {
            return ResponseEntity.status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data());
    }
}
