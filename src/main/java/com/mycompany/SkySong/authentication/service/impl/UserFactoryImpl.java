package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.service.UserFactory;

public class UserFactoryImpl implements UserFactory {
    @Override
    public User createUser(RegisterRequest registerRequest, Role role) {
        return null;
    }
}
