package com.mycompany.SkySong.application.user.registration.ports;

import com.mycompany.SkySong.domain.shared.entity.User;

public interface UserSaver {
    void saveUser(User user);
}
