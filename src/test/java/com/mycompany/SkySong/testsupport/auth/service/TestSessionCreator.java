package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.user.Session;
import java.util.Date;

public class TestSessionCreator {

    public Session createSession() {
        Session session = new Session();
        session.setSessionId("xAUpqIbS2L9_ULU39L7Z007RJufNgFizawVK68qTyrw=");
        session.setUserId(1);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
        return session;
    }
}
