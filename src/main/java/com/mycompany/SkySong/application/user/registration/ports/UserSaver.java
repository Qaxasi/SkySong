package com.mycompany.SkySong.application.user.registration.ports;

import com.mycompany.SkySong.application.user.registration.dto.UserSaveDto;

public interface UserSaver {
    void saveUser(UserSaveDto userDto);
}
