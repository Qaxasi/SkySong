package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;

public interface SessionCreation {
    Session createSession(Integer userID);
}
