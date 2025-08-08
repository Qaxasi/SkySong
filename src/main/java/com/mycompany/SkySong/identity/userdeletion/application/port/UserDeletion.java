package com.mycompany.SkySong.identity.userdeletion.application.port;

import com.mycompany.SkySong.shared.result.Result;

public interface UserDeletion {
    Result<Void> deleteEverythingById(int id);
}
