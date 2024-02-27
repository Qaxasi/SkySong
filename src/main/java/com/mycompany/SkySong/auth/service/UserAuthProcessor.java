package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.User;

public interface UserAuthProcessor {
    User authenticateAndRetrieveUser(LoginRequest request);
}
