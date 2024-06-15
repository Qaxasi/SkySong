package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.common.entity.User;

public interface UserSaver {
    void saveUser(User user);
}
