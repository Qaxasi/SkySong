package com.mycompany.SkySong.auth.security;

public interface SessionUserInfoProvider {

    String getUsernameForSession(String sessionId);
}
