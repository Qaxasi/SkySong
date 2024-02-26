package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationManagerImpl implements UserRegistrationManager {

    private final UserPersistence userPersistence;
    private final UserWithRoleSetup userSetup;

    public UserRegistrationManagerImpl(UserPersistence userPersistence, UserWithRoleSetup userSetup) {
        this.userPersistence = userPersistence;
        this.userSetup = userSetup;
    }

    @Override
    public void setupNewUser(RegisterRequest request) {
        User user = userSetup.setupUserWithRole(request);
        userPersistence.saveUser(user);
    }
}
