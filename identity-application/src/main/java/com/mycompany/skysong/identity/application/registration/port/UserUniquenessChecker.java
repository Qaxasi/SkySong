package com.mycompany.skysong.identity.application.registration.port;

import com.mycompany.skysong.identity.application.registration.model.UniquenessStatus;

public interface UserUniquen {
    UniquenessStatus checkUniqueness(String username, String email);
}
