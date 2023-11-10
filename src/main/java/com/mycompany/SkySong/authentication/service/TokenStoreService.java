package com.mycompany.SkySong.authentication.service;

public interface TokenStoreService {
    void blacklistToken(String token);
}
