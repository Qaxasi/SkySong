package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.ApiResponse;
import com.mycompany.SkySong.authentication.model.dto.DeleteResponse;

public interface DeleteUserService {
    ApiResponse deleteUser(long userId);
}
