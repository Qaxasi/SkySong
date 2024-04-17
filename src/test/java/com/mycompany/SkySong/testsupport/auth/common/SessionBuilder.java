package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.user.Session;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SessionBuilder {

    private String sessionId;
    private Integer userId;

    private SessionBuilder() {}

    public static SessionBuilder aSession() {
        return new SessionBuilder();
    }

    public SessionBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SessionBuilder withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public SessionBuilder buildBySessionId(String sessionId) {
        return withSessionId(sessionId).withUserId(1);
    }

    public SessionBuilder buildByUserId(Integer userId) {
        return withUserId(userId).withSessionId("12345abcdefgh");
    }

    public Session build() {
        Session session = new Session();
        session.setUserId(userId);
        session.setSessionId(sessionId);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
        return session;
    }
}