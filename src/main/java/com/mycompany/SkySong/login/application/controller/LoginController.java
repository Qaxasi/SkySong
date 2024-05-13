package com.mycompany.SkySong.login.application.controller;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.login.domain.service.LoginFacade;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LoginController {

    private final LoginFacade login;

    public LoginController(LoginFacade login) {
        this.login = login;
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request,
                                             HttpServletResponse response) {
        String sessionToken = login.login(request);

        Cookie sessionCookie = new Cookie("session_id", sessionToken);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        return ResponseEntity.ok(new ApiResponse("Logged successfully."));
    }
}
