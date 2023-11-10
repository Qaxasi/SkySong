package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.entity.BlacklistedToken;
import com.mycompany.SkySong.authentication.repository.BlacklistedTokenDAO;
import com.mycompany.SkySong.authentication.service.TokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;

public class TokenStoreServiceImpl implements TokenStoreService {
    @Autowired
    private BlacklistedTokenDAO blacklistedTokenDAO;
    @Override
    public void blacklistToken(String token) {
        if (!blacklistedTokenDAO.existsByToken(token)) {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            blacklistedTokenDAO.save(blacklistedToken);
        }
    }
}
