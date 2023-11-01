package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.service.DeleteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final DeleteService deleteService;

    public UserController(DeleteService deleteService) {
        this.deleteService = deleteService;
    }
}
