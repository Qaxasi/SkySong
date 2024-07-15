package com.mycompany.SkySong.application.user.delete.port.in;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;

public interface UserDeletion {
    ApiResponse delete(int userId);
}
