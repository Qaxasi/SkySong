package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.User;

public interface UserWithRoleSetup {

    User setupUserWithRole(RegisterRequest request);
}
