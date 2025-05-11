package com.mycompany.SkySong.shared.config.cookie;

public class CookieProperties {
    private String name;
    private String path;
    private int maxAge;
    private boolean httpOnly = true;
    private boolean secure = true;
    private String sameSite = "Strict";


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }
}
