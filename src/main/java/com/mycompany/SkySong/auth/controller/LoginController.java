package com.mycompany.SkySong.auth.controller;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.service.ApplicationMessageLoader;
import com.mycompany.SkySong.auth.service.UserSessionCreator;
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

    private final UserSessionCreator session;
    private final ApplicationMessageLoader message;

    public LoginController(UserSessionCreator session,
                           ApplicationMessageLoader message) {
        this.session = session;
        this.message = message;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                             HttpServletResponse response) {
        String sessionToken = login.login(loginRequest);

        Cookie sessionCookie = new Cookie("session_id", sessionToken);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        return ResponseEntity.ok(new ApiResponse(message.getMessage("login.success")));
    }
}
