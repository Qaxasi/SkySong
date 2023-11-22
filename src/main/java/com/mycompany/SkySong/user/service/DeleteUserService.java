package com.mycompany.SkySong.user.service;

import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.dto.ApiResponse;

public interface DeleteUserService {
    ApiResponse deleteUser(long userId) throws DatabaseException;
}
