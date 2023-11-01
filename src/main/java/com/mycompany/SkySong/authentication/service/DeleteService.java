package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;

public interface DeleteService {
    DeleteResponse deleteUser(long userId);
}
