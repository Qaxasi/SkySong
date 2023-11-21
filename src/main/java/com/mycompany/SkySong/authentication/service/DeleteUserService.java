package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.DatabaseException;
import com.mycompany.SkySong.authentication.model.dto.ApiResponse;

public interface DeleteUserService {
    ApiResponse deleteUser(long userId) throws DatabaseException;
}
