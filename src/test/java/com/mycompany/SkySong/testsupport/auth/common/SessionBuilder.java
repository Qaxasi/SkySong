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
}