package com.mycompany.SkySong.domain.shared.entity;

import java.util.Date;

public class Session {

    private String sessionId;
    private Integer userId;
    private Date createAt;
    private Date expiresAt;

    public Session() {}

    public Session(String sessionId, Integer userId, Date createAt, Date expiresAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.createAt = createAt;
        this.expiresAt = expiresAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
