package com.mycompany.SkySong.logout.domain.ports;

import com.mycompany.SkySong.common.infrastructure.TokenHasher;

public interface SessionTokenHasher extends TokenHasher {
    String hashToken(String token);
}
