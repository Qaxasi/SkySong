package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.DatabaseException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;

public interface DeleteUserService {
    ApiResponse deleteUser(int userId) throws DatabaseException;
}
