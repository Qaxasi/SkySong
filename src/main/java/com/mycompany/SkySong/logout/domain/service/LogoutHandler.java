package com.mycompany.SkySong.logout.domain.service;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.logout.domain.ports.SessionRepositoryPort;

class LogoutHandler {

    private final SessionRepositoryPort sessionRepositoryPort;
    private final TokenHasher tokenHasher;
    LogoutHandler(SessionRepositoryPort sessionRepositoryPort,
                  TokenHasher tokenHasher) {
        this.sessionRepositoryPort = sessionRepositoryPort;
        this.tokenHasher = tokenHasher;
    }
}
