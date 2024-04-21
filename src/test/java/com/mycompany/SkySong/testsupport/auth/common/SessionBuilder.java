package com.mycompany.SkySong.testsupport.auth.common;

import com.mycompany.SkySong.user.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope("prototype")
public class SessionBuilder {

    private String sessionId = "12345abcdefg";
    private Integer userId = 1;

    public SessionBuilder withSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SessionBuilder withUserId(Integer userId) {
        this.userId = userId;
        return this;
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