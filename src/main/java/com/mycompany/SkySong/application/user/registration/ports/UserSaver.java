package com.mycompany.SkySong.application.registration.ports;

import com.mycompany.SkySong.application.registration.dto.UserSaveDto;

public interface UserSaver {
    void saveUser(UserSaveDto userDto);
}
