package com.mycompany.SkySong.domain.registration.ports;

import com.mycompany.SkySong.application.registration.dto.UserSaveDto;

public interface UserSaver {
    void saveUser(UserSaveDto userDto);
}
