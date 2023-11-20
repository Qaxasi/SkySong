package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;

public interface UserFactory {
    User createUser(RegisterRequest registerRequest, Role role);
}
