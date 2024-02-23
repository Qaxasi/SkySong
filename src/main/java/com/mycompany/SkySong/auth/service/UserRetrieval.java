package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;
import org.springframework.security.core.Authentication;

public interface UserRetrieval {
    User findUserByAuthentication(Authentication authentication);
}
