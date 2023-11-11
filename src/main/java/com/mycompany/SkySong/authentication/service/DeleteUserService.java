package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;

public interface DeleteUserService {
    DeleteResponse deleteUser(long userId);
}
