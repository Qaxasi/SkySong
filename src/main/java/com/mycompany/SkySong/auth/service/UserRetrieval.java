package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.User;

public interface UserRetrieval {
    User findByAuthUsername(String username);
}
