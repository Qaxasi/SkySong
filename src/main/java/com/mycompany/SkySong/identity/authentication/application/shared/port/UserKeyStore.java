package com.mycompany.SkySong.identity.authentication.application.shared.port;

import java.util.Optional;

public interface UserKeyStore {
    Optional<byte[]> getForUser(int userId);
}
