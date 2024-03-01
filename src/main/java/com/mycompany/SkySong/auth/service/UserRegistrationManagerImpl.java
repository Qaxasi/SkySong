package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationManagerImpl implements UserRegistrationManager {

    private final UserSaver userSaver;
    private final UserWithRoleSetup userSetup;

    public UserRegistrationManagerImpl(UserSaver userSaver, UserWithRoleSetup userSetup) {
        this.userSaver = userSaver;
        this.userSetup = userSetup;
    }

    @Override
    @Transactional
    public void setupNewUser(RegisterRequest request) {
        User user = userSetup.setupUserWithRole(request);
        userSaver.saveUser(user);
    }
}
