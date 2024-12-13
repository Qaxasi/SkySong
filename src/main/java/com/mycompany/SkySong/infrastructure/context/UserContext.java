package com.mycompany.SkySong.infrastructure.context;

public class UserContext {
    private static final ThreadLocal<Integer> userId = new ThreadLocal<>();

    public static void setUserId(int id) {
        userId.set(id);
    }

    public static Integer getUserId() {
        return userId.get();
    }

    public static void clear() {
        userId.remove();
    }
}
